@file:Suppress("unused")

package app.umcu.api.remote

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

//region Lists
enum class MediaType(@JsonValue val value: String) {
	Movie("movie"), TV("tv")
}

data class TMDBListItem(
	val id: Int, val title: String?, @JsonProperty("media_type") val mediaType: MediaType?
)

data class TMDBListDetails(
	val id: String,
	@JsonProperty("iso_639_1") val iso6391: String?,
	@JsonProperty("item_count") val itemCount: Int?,
	val items: List<TMDBListItem>
)
//endregion

//region Movies
data class TMDBMovieDetails(
	val homepage: String?,
	val id: Int,
	@JsonProperty("imdb_id") val imdbId: String?,
	val overview: String?,
	@JsonProperty("poster_path") val posterPath: String?,
	@JsonProperty("release_date") val releaseDate: String?,
	val title: String?
)

enum class ReleaseType(@JsonValue val value: Int) {
	Premiere(1), LimitedTheatrical(2), Theatrical(3), Digital(4), Physical(5), TV(6)
}

data class TMDBMovieReleaseDatesItem(
	@JsonProperty("iso_3166_1") val iso31661: String?, @JsonProperty("release_date") val releaseDates: List<ReleaseDate>
) {
	data class ReleaseDate(
		val certification: String?, @JsonProperty("release_date") val releaseDate: String?, val type: ReleaseType?
	)
}

data class TMDBMovieReleaseDates(
	val id: Int, val results: List<TMDBMovieReleaseDatesItem>
)
//endregion

//region TV
data class TVSeasonDetails(
	@JsonProperty("air_date") val airDate: String?,
	val name: String?,
	val overview: String?,
	val id: Int,
	@JsonProperty("poster_path") val posterPath: String?,
	@JsonProperty("season_number") val seasonNumber: Int?
)

data class TVSeriesDetails(
	@JsonProperty("first_air_date") val firstAirDate: String?,
	val homepage: String?,
	val id: Int,
	val name: String?,
	@JsonProperty("number_of_episodes") val numberOfEpisodes: Int?,
	@JsonProperty("number_of_seasons") val numberOfSeasons: Int?,
	val overview: String?,
	@JsonProperty("poster_path") val posterPath: String?,
	val seasons: List<TVSeasonDetails>
)
//endregion
