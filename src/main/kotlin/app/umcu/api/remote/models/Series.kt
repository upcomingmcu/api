package app.umcu.api.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object Series {
	@Serializable
	data class Details(
		@SerialName("first_air_date") val firstAirDate: String?,
		@SerialName("id") val seriesId: Int,
		val name: String,
		val overview: String?,
		@SerialName("poster_path") val posterPath: String?,
		val seasons: List<Season>
	)

	@Serializable
	data class Season(
		@SerialName("air_date") val airDate: String?,
		@SerialName("episode_count") val episodeCount: Int?,
		@SerialName("id") val seasonId: Int,
		val name: String,
		val overview: String?,
		@SerialName("poster_path") val posterPath: String?,
		@SerialName("season_number") val seasonNumber: Int
	)
}
