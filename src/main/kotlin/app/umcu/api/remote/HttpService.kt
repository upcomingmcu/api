package app.umcu.api.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class HttpService {
	val client = HttpClient(CIO) {
		install(ContentNegotiation) {
			json(Json {
				ignoreUnknownKeys = true
				isLenient = true
			})
		}
	}

	suspend inline fun <reified T> makeRequest(
		urlString: String, method: HttpMethod, block: HttpRequestBuilder.() -> Unit
	): T = client.request(urlString) {
		this.method = method
		block()
	}.body<T>()

	suspend inline fun <reified T> makeGetRequest(urlString: String, block: HttpRequestBuilder.() -> Unit) =
		makeRequest<T>(
			urlString, HttpMethod.Get, block
		)
}
