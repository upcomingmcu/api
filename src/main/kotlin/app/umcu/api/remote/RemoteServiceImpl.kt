package app.umcu.api.remote

import app.umcu.api.remote.models.Lists
import app.umcu.api.remote.models.Movies
import app.umcu.api.remote.models.ReleaseDates
import app.umcu.api.remote.models.Series
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RemoteServiceImpl(
	tmdbApiKey: String
) : RemoteService {
	private val httpService by inject<HttpService>(HttpService::class.java)

	@Suppress("unused")
	private val logger: Logger = LoggerFactory.getLogger(this::class.java.simpleName)

	override val baseUrl: String = "https://api.themoviedb.org/3"
	override val authKey: String = tmdbApiKey

	override suspend fun getListDetails(listId: Int) = httpService.makeGetRequest<Lists.Details>(baseUrl) {
		bearerAuth(authKey)
		url {
			appendPathSegments("list", listId.toString())
		}
	}

	override suspend fun getMovieDetails(movieId: Int) = httpService.makeGetRequest<Movies.Details>(baseUrl) {
		bearerAuth(authKey)
		url {
			appendPathSegments("movie", movieId.toString())
		}
	}

	override suspend fun getMovieReleaseDates(movieId: Int) =
		httpService.makeGetRequest<ReleaseDates.Details>(baseUrl) {
			bearerAuth(authKey)
			url {
				appendPathSegments("movie", movieId.toString(), "release_dates")
			}
		}

	override suspend fun getSeriesDetails(seriesId: Int) = httpService.makeGetRequest<Series.Details>(baseUrl) {
		bearerAuth(authKey)
		url {
			appendPathSegments("tv", seriesId.toString())
		}
	}

	override fun collectData() = runBlocking {
		// TODO
	}
}
