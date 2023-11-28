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
import app.umcu.api.features.productions.repository.ProductionsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class TMDBService(
	private val restTemplate: RestTemplate = RestTemplate(), private val productionsRepository: ProductionsRepository
) : ApplicationRunner {
	private val apiToken: String = System.getenv()["TMDB_READ_ACCESS_TOKEN"] ?: ""
	private val logger: Logger = LoggerFactory.getLogger(TMDBService::class.java)
	private val tmdbListId = 8254729 // TMDB list of all MCU productions

	private inline fun <reified T : Any> get(url: String): T? {
		val headers = HttpHeaders()
		headers.setBearerAuth(apiToken)
		val response: ResponseEntity<T> = restTemplate.exchange(url,
			HttpMethod.GET,
			HttpEntity("body", headers),
			object : ParameterizedTypeReference<T>() {})
		return response.body
	}

	private inline fun <reified T : Any> getFromTMDB(endpoint: String): T? {
		val url = "https://api.themoviedb.org/3/$endpoint"
		logger.info("GET $url")
		return get<T>(url)
	}

	private fun parseDate(dateString: String?, longForm: Boolean = false): LocalDate? {
		val pattern = if (longForm) "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" else "yyyy-MM-dd"
		return dateString?.takeIf { it.isNotEmpty() }?.let {
			return LocalDate.parse(it, DateTimeFormatter.ofPattern(pattern))
		}
	}

	private fun getListDetails(@Suppress("SameParameterValue") listId: Int, page: Int = 1): TMDBListDetails? {
		return getFromTMDB<TMDBListDetails>("list/$listId?page=$page")
	}

	private fun getListItems(): ArrayList<TMDBListItem>? {
		val listItems: ArrayList<TMDBListItem> = ArrayList()
		var page = 1
		do {
			val tmdbListDetails = getListDetails(tmdbListId, page) ?: return null
			listItems.addAll(tmdbListDetails.items)
			page += 1
		} while (tmdbListDetails.page != tmdbListDetails.totalPages)
		return if (listItems.size <= 0) null else listItems
	}

	private fun getMovieDetails(movieId: Int): TMDBMovieDetails? {
		return getFromTMDB<TMDBMovieDetails>("movie/$movieId")
	}

	private fun getMovieReleaseDates(movieId: Int): TMDBMovieReleaseDates? {
		return getFromTMDB<TMDBMovieReleaseDates>("movie/$movieId/release_dates")
	}

	private fun getSeriesDetails(seriesId: Int): TMDBTVSeriesDetails? {
		return getFromTMDB<TMDBTVSeriesDetails>("tv/$seriesId")
	}

	private fun getReleaseDateByRegion(movieDetails: TMDBMovieDetails, iso31661: String = "US"): String? {
		val movieReleaseDates = getMovieReleaseDates(movieDetails.id) ?: return null
		val releaseDates =
			movieReleaseDates.results.firstOrNull { it.iso31661 == iso31661 }!!.releaseDates.firstOrNull { it.type == ReleaseType.Theatrical }
				?: return null
		return releaseDates.releaseDate
	}

	private fun getPosterUrl(posterPath: String?): String? {
		return if (posterPath.isNullOrEmpty()) null else "https://image.tmdb.org/t/p/original/${posterPath}"
	}

	private fun handleMovies(listItems: Iterable<TMDBListItem>): ArrayList<Production> {
		val productions: ArrayList<Production> = ArrayList()
		listItems.filter { it.mediaType == MediaType.Movie }.forEach { item ->
			val movieDetails = getMovieDetails(item.id)
			if (movieDetails != null) {
				val releaseDates = if (!movieDetails.releaseDate.isNullOrEmpty()) getReleaseDateByRegion(
					movieDetails
				) else null
				val parsedReleaseDate = parseDate(releaseDates ?: movieDetails.releaseDate, releaseDates != null)

				val production = Production(
					tmdbId = movieDetails.id,
					title = movieDetails.title,
					overview = movieDetails.overview,
					releaseDate = parsedReleaseDate,
					mediaType = Production.MediaType.MOVIE,
					poster = getPosterUrl(movieDetails.posterPath)
				)
				productions.add(production)
			}
		}
		return productions
	}

	private fun handleSeries(listItems: Iterable<TMDBListItem>): ArrayList<Production> {
		val productions: ArrayList<Production> = ArrayList()
		listItems.filter { it.mediaType == MediaType.TV }.forEach { item ->
			val seriesDetails = getSeriesDetails(item.id)
			if (seriesDetails != null) {
				if (seriesDetails.seasons.size > 1) {
					seriesDetails.seasons.forEach { seasonDetails ->
						val overview =
							if (seasonDetails.overview.isNullOrEmpty()) seriesDetails.overview else seasonDetails.overview

						val production = Production(
							tmdbId = seriesDetails.id,
							title = "${seriesDetails.name} Season ${seasonDetails.seasonNumber}",
							overview = overview,
							releaseDate = parseDate(seasonDetails.airDate),
							mediaType = Production.MediaType.TV,
							poster = getPosterUrl(seasonDetails.posterPath)
						)
						productions.add(production)
					}
				} else {
					val seasonDetails = seriesDetails.seasons.first()
					val overview =
						if (seasonDetails.overview.isNullOrEmpty()) seriesDetails.overview else seasonDetails.overview

					val production = Production(
						tmdbId = seriesDetails.id,
						title = seriesDetails.name,
						overview = overview,
						releaseDate = parseDate(seasonDetails.airDate),
						mediaType = Production.MediaType.TV,
						poster = getPosterUrl(seasonDetails.posterPath)
					)
					productions.add(production)
				}
			}
		}
		return productions
	}

	override fun run(args: ApplicationArguments?) {
		val listItems = getListItems() ?: return
		val productions: ArrayList<Production> = ArrayList()
		handleMovies(listItems).let {
			productions.addAll(it)
		}
		handleSeries(listItems).let {
			productions.addAll(it)
		}
		productionsRepository.saveAll(productions)
	}
}
