package app.umcu.api.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object ReleaseDates {
	@Serializable
	enum class ReleaseType {
		@SerialName("1")
		PREMIERE,

		@SerialName("2")
		LIMITED_THEATRICAL,

		@SerialName("3")
		THEATRICAL,

		@SerialName("4")
		DIGITAL,

		@SerialName("5")
		PHYSICAL,

		@SerialName("6")
		TV
	}

	@Serializable
	data class Details(
		@SerialName("id") val movieId: Int, val results: List<Result>
	)

	@Serializable
	data class Result(
		@SerialName("iso_3166_1") val iso31661: String, @SerialName("release_dates") val releaseDates: List<ReleaseDate>
	)

	@Serializable
	data class ReleaseDate(
		val certification: String, @SerialName("release_date") val releaseDate: String, val type: ReleaseType
	)
}
