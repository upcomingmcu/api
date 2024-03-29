package app.umcu.api.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object Movies {
	@Serializable
	data class Details(
		@SerialName("id") val movieId: Int,
		val overview: String?,
		@SerialName("poster_path") val posterPath: String?,
		@SerialName("release_date") val releaseDate: String?,
		val title: String
	)
}
