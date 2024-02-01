package app.umcu.api.remote

import app.umcu.api.remote.models.Lists
import app.umcu.api.remote.models.Movies
import app.umcu.api.remote.models.ReleaseDates
import app.umcu.api.remote.models.Series

interface RemoteService {
	val baseUrl: String
	val authKey: String
	suspend fun getListDetails(listId: Int, page: Int = 1): Lists.Details
	suspend fun getListItems(listId: Int): ArrayList<Lists.Item>
	suspend fun getMovieDetails(movieId: Int): Movies.Details
	suspend fun getMovieReleaseDates(movieId: Int): ReleaseDates.Details
	suspend fun getSeriesDetails(seriesId: Int): Series.Details
	fun collectData()
}
