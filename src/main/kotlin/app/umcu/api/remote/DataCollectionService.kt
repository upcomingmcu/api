package app.umcu.api.remote

import app.umcu.api.features.productions.models.Production
import app.umcu.api.remote.models.Lists
import app.umcu.api.remote.models.Movies
import app.umcu.api.remote.models.ReleaseDates
import app.umcu.api.remote.models.Series

typealias ReleaseDatesPair = Pair<Int, List<ReleaseDates.Result>>

interface DataCollectionService {
	val listId: Int
	val listItems: ArrayList<Lists.Item>
	suspend fun collectMovies(): List<Movies.Details>
	suspend fun collectMovieReleaseDates(movies: List<Movies.Details>): ArrayList<ReleaseDatesPair>
	suspend fun collectSeries(): List<Series.Details>
	suspend fun collectAllMovies(): List<Production>
	suspend fun collectAllSeries(): List<Production>
}
