package app.umcu.api.remote

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

@Component
class TMDBService(
	private val restTemplate: RestTemplate = RestTemplate(),
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

	override fun run(args: ApplicationArguments?) {
		TODO()
	}
}
