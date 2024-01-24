package app.umcu.api.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object ReleaseDates {
	@Serializable
	enum class ReleaseType(value: Int) {
		@SerialName("1")
		PREMIERE(1),

		@SerialName("2")
		LIMITED_THEATRICAL(2),

		@SerialName("3")
		THEATRICAL(3),

		@SerialName("4")
		DIGITAL(4),

		@SerialName("5")
		PHYSICAL(5),

		@SerialName("6")
		TV(6)
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
