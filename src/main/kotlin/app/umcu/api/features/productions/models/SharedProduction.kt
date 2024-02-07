package app.umcu.api.features.productions.models

import kotlinx.datetime.Instant

interface SharedProduction {
	val slug: String
	val tmdbId: Int
	val title: String
	val overview: String?
	val releaseDate: Instant?
	val posterUrl: String?
	val mediaType: String
}
