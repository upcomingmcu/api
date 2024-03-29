@file:Suppress("unused")

package app.umcu.api.remote.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

object Lists {
	@Serializable
	enum class MediaType {
		@SerialName("movie")
		MOVIE,

		@SerialName("tv")
		TV
	}

	@Serializable
	data class Item @OptIn(ExperimentalSerializationApi::class) constructor(
		val id: Int, @JsonNames("title", "name") val title: String, @SerialName("media_type") val mediaType: MediaType
	)

	@Serializable
	data class Details(
		val id: Int,
		@SerialName("item_count") val itemCount: Int,
		val items: List<Item>,
		val page: Int,
		@SerialName("total_pages") val totalPages: Int
	)
}
