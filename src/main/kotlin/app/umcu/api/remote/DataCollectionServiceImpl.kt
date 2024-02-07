package app.umcu.api.remote

import app.umcu.api.features.productions.models.Production
import app.umcu.api.remote.models.Lists
import app.umcu.api.remote.models.Movies
import app.umcu.api.remote.models.Series
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent

class DataCollectionServiceImpl : DataCollectionService {
	private val remoteService: RemoteService by KoinJavaComponent.inject(RemoteServiceImpl::class.java)

	override val listId: Int = 8289533

	override val listItems: ArrayList<Lists.Item> = runBlocking {
		remoteService.getListItems(listId)
	}

	override suspend fun collectMovies(): List<Movies.Details> {
		val movies = ArrayList<Movies.Details>()
		val movieIds = listItems.filter { it.mediaType == Lists.MediaType.MOVIE }.map { it.id }
		movieIds.forEach { id -> movies.add(remoteService.getMovieDetails(id)) }
		return movies
	}

	override suspend fun collectMovieReleaseDates(movies: List<Movies.Details>): ArrayList<ReleaseDatesPair> {
		val releaseDates = ArrayList<ReleaseDatesPair>()
		val movieIds = listItems.filter { it.mediaType == Lists.MediaType.MOVIE }.map { it.id }
		movieIds.forEach { id -> releaseDates.add(Pair(id, remoteService.getMovieReleaseDates(id).results)) }
		return releaseDates
	}

	override suspend fun collectSeries(): List<Series.Details> {
		val series = ArrayList<Series.Details>()
		val seriesIds = listItems.filter { it.mediaType == Lists.MediaType.TV }.map { it.id }
		seriesIds.forEach { id -> series.add(remoteService.getSeriesDetails(id)) }
		return series
	}

	override suspend fun collectAllMovies(): List<Production> {
		val movies = collectMovies()
		// TODO Get the movies theatrical release date from `collectMovieReleaseDates`.
		//		Convert `releaseDate` String to Instant.
		return movies.map { Production(it.movieId, it.title, it.overview, null, it.posterPath, "movie") }
	}

	override suspend fun collectAllSeries(): List<Production> {
		// TODO	Get all series and their seasons.
		//		Map each season to a `Production` object rather than the series itself.
		TODO()
	}
}
