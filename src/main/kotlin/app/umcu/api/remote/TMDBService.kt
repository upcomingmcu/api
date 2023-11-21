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

	private inline fun <reified T : Any> get(@Suppress("SameParameterValue") url: String): T? {
		val headers = HttpHeaders()
		headers.setBearerAuth(apiToken)
		val response: ResponseEntity<T> = restTemplate.exchange(
			url,
			HttpMethod.GET,
			HttpEntity("body", headers),
			object : ParameterizedTypeReference<T>() {})
		return response.body
	}

	private fun getListDetails(@Suppress("SameParameterValue") listId: Int, page: Int = 1): TMDBListDetails? {
		return get<TMDBListDetails>("https://api.themoviedb.org/3/list/$listId?page=$page")
	}

	private fun getProductions(): ArrayList<Production> {
		val listId = 8254729 // TMDB list of all MCU productions
		val productions: ArrayList<Production> = ArrayList()
		var page = 1
		do {
			val tmdbListDetails = getListDetails(listId, page) ?: return ArrayList()
			tmdbListDetails.items.forEach { item ->
				if (item.mediaType == MediaType.Movie) {
					val tmdbMovieDetails =
						get<TMDBMovieDetails>("https://api.themoviedb.org/3/movie/${item.id}") ?: return ArrayList()

					val production = Production(tmdbId = tmdbMovieDetails.id,
						title = tmdbMovieDetails.title,
						releaseDate = tmdbMovieDetails.releaseDate?.takeIf { it.isNotEmpty() }?.let {
							LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
						}

					)
					logger.info("Added production \"${production.slug}\"")
					productions.add(production)
				}
			}
			page += 1
		} while (tmdbListDetails.page != tmdbListDetails.totalPages)
		return productions
	}

	override fun run(args: ApplicationArguments?) {
		/**
		 * TODO
		 * 	- Break down code into more functions
		 * 	- Handle parsing TV productions (inc. multiple seasons)
		 */
		val productions = getProductions()
		productionsRepository.saveAll(productions)
	}
}
