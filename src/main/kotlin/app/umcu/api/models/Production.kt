package app.umcu.api.models

import app.umcu.api.utils.extensions.toSlug
import kotlinx.serialization.Serializable

@Serializable
data class Production(
	val slug: String, val tmdbId: Int, val title: String
) {
	constructor(tmdbId: Int, title: String) : this(slug = title.toSlug(), tmdbId, title)

	companion object
}

val Production.Companion.sampleDataSet: Array<Production>
	get() = arrayOf(Production(1726, "Iron Man"), Production(10138, "Iron Man 2"), Production(68721, "Iron Man 3"))
