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

	/**
	 * Get the details of a list.
	 * You can only get 20 items in a list a time, so you must loop through each page to get all the items.
	 *
	 * [TMDB API Reference](https://developer.themoviedb.org/reference/list-details)
	 *
	 * @param listId The ID of the list.
	 * @param page The page of items in the list.
	 * @return [Lists.Details] The details of the list.
	 */
	override suspend fun getListDetails(listId: Int, page: Int) = httpService.makeGetRequest<Lists.Details>(baseUrl) {
		bearerAuth(authKey)
		url {
			appendPathSegments("list", listId.toString())
		}
	}

	/**
	 * Get all the items in a list.
	 * Uses [getListDetails] to loop through each page in the list to collect all items.
	 *
	 * @param listId The ID of the list.
	 * @return Array list of [Lists.Item].
	 */
	override suspend fun getListItems(listId: Int): ArrayList<Lists.Item> {
		val items = ArrayList<Lists.Item>()
		var page = 1
		do {
			val details = getListDetails(listId, page)
			items.addAll(details.items)
			page += 1
		} while (details.page != details.totalPages)
		return items
	}

	/**
	 * Get the details of a movie.
	 *
	 * [TMDB API Reference](https://developer.themoviedb.org/reference/movie-details)
	 *
	 * @param movieId The ID of the movie.
	 * @return [Movies.Details]
	 */
	override suspend fun getMovieDetails(movieId: Int) = httpService.makeGetRequest<Movies.Details>(baseUrl) {
		bearerAuth(authKey)
		url {
			appendPathSegments("movie", movieId.toString())
		}
	}

	/**
	 * Get the release dates of a movie.
	 *
	 * [TMDB API Reference](https://developer.themoviedb.org/reference/movie-release-dates)
	 *
	 * @param movieId The ID of the movie.
	 * @return [ReleaseDates.Details]
	 */
	override suspend fun getMovieReleaseDates(movieId: Int) =
		httpService.makeGetRequest<ReleaseDates.Details>(baseUrl) {
			bearerAuth(authKey)
			url {
				appendPathSegments("movie", movieId.toString(), "release_dates")
			}
		}

	/**
	 * Get the details of a TV series.
	 *
	 * [TMDB API Reference](https://developer.themoviedb.org/reference/tv-series-details)
	 *
	 * @param seriesId The ID of the TV series.
	 * @return [Series.Details]
	 */
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
