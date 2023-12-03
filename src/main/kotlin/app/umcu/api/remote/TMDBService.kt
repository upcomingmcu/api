/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

@file:Suppress("SpellCheckingInspection", "unused")

package app.umcu.api.remote

import app.umcu.api.features.productions.model.Production
import app.umcu.api.features.productions.model.ProductionMediaType
import app.umcu.api.features.productions.repository.ProductionsRepository
import app.umcu.api.utils.DateParsingUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class TMDBService(
	restTemplate: RestTemplate = RestTemplate(), private val productionsRepository: ProductionsRepository
) : ApplicationRunner {
	private val tmdbWrapper = TMDBWrapper(restTemplate)
	private val dateParsingUtils = DateParsingUtils()
	private val tmdbListId = 8254729 // https://www.themoviedb.org/list/8254729-mcu
	private val logger: Logger = LoggerFactory.getLogger(TMDBService::class.java)

	private fun handleMovies(listItems: Iterable<TMDBListItem>): ArrayList<Production> {
		val productions: ArrayList<Production> = ArrayList()

		listItems.filter { it.mediaType == MediaType.Movie }.forEach { item ->
			val movieDetails = tmdbWrapper.getMovieDetails(item.id) ?: return@forEach

			val releaseDates = if (!movieDetails.releaseDate.isNullOrEmpty()) tmdbWrapper.getReleaseDateByRegion(
				movieDetails
			) else null
			val parsedReleaseDate = dateParsingUtils.parseZonedDateTime(releaseDates ?: movieDetails.releaseDate)

			productions.add(
				Production(
					tmdbId = movieDetails.id,
					title = movieDetails.title,
					overview = movieDetails.overview,
					releaseDate = parsedReleaseDate,
					mediaType = ProductionMediaType.MOVIE,
					poster = tmdbWrapper.getPosterUrl(movieDetails.posterPath)
				)
			)
		}

		return productions
	}

	private fun handleSeries(listItems: Iterable<TMDBListItem>): ArrayList<Production> {
		val productions: ArrayList<Production> = ArrayList()

		listItems.filter { it.mediaType == MediaType.TV }.forEach { item ->
			val seriesDetails = tmdbWrapper.getSeriesDetails(item.id) ?: return@forEach

			if (seriesDetails.seasons.size > 1) {
				seriesDetails.seasons.forEach { seasonDetails ->
					val overview =
						if (seasonDetails.overview.isNullOrEmpty()) seriesDetails.overview else seasonDetails.overview

					productions.add(
						Production(
							tmdbId = seriesDetails.id,
							title = "${seriesDetails.name} Season ${seasonDetails.seasonNumber}",
							overview = overview,
							releaseDate = dateParsingUtils.parseZonedDateTime(seasonDetails.airDate),
							mediaType = ProductionMediaType.TV,
							poster = tmdbWrapper.getPosterUrl(seasonDetails.posterPath)
						)
					)
				}
			} else {
				val seasonDetails = seriesDetails.seasons.first()
				val overview =
					if (seasonDetails.overview.isNullOrEmpty()) seriesDetails.overview else seasonDetails.overview

				productions.add(
					Production(
						tmdbId = seriesDetails.id,
						title = seriesDetails.name,
						overview = overview,
						releaseDate = dateParsingUtils.parseZonedDateTime(seasonDetails.airDate),
						mediaType = ProductionMediaType.TV,
						poster = tmdbWrapper.getPosterUrl(seasonDetails.posterPath)
					)
				)
			}
		}

		return productions
	}

	override fun run(args: ApplicationArguments?) {
		val productions: ArrayList<Production> = ArrayList()
		val listItems = tmdbWrapper.getListItems(tmdbListId) ?: return

		handleMovies(listItems).let { productions.addAll(it) }
		handleSeries(listItems).let { productions.addAll(it) }

		val savedProductions = productionsRepository.saveAll(productions)
		logger.info("Saved ${savedProductions.size} productions to the database.")
	}
}
