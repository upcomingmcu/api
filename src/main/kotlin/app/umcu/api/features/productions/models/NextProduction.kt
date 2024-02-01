package app.umcu.api.features.productions.models

import app.umcu.api.utils.ext.toSlug
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NextProduction(
	override val slug: String,
	@SerialName("tmdb_id") override val tmdbId: Int,
	override val title: String,
	@SerialName("release_date") override val releaseDate: Instant?,
	val next: String?
) : SharedProduction {
	constructor(tmdbId: Int, title: String, releaseDate: Instant? = null, next: String? = null) : this(
		slug = title.toSlug(), tmdbId, title, releaseDate, next
	)
}
