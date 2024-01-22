package app.umcu.api.models

import app.umcu.api.utils.extensions.toSlug
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Production(
	val slug: String, val tmdbId: Int, val title: String, val releaseDate: Instant?
) {
	constructor(tmdbId: Int, title: String, releaseDate: Instant? = null) : this(
		slug = title.toSlug(), tmdbId, title, releaseDate
	)
}
