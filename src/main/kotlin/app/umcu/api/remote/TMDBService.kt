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

@Suppress("unused")
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
		val response: ResponseEntity<T> = restTemplate.exchange(
			url,
			HttpMethod.GET,
			HttpEntity("body", headers),
			object : ParameterizedTypeReference<T>() {})
		return response.body
	}

	private inline fun <reified T : Any> getTmdb(endpoint: String): T? {
		val url = "https://api.themoviedb.org/3/$endpoint"
		logger.info("GET $url")
		return get<T>(url)
	}

	private fun parseDate(dateString: String?): LocalDate? {
		return dateString?.takeIf { it.isNotEmpty() }?.let {
			return LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
		}
	}

	private fun getListDetails(@Suppress("SameParameterValue") listId: Int, page: Int = 1): TMDBListDetails? {
		return getTmdb<TMDBListDetails>("list/$listId?page=$page")
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
		return getTmdb<TMDBMovieDetails>("movie/${movieId}")
	}

	private fun getSeriesDetails(seriesId: Int): TMDBTVSeriesDetails? {
		return getTmdb<TMDBTVSeriesDetails>("tv/${seriesId}")
	}

	private fun handleMovies(listItems: Iterable<TMDBListItem>): ArrayList<Production> {
		val productions: ArrayList<Production> = ArrayList()
		listItems.filter { it.mediaType == MediaType.Movie }.forEach { item ->
			val movieDetails = getMovieDetails(item.id)
			if (movieDetails != null) {
				val production = Production(
					tmdbId = movieDetails.id,
					title = movieDetails.title,
					releaseDate = parseDate(movieDetails.releaseDate)
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
						val production = Production(
							tmdbId = seriesDetails.id,
							title = "${seriesDetails.name} Season ${seasonDetails.seasonNumber}",
							releaseDate = parseDate(seasonDetails.airDate)
						)
						productions.add(production)
					}
				} else {
					val season = seriesDetails.seasons.first()
					val production = Production(
						tmdbId = seriesDetails.id, title = seriesDetails.name, releaseDate = parseDate(season.airDate)
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
