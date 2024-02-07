package app.umcu.api.features.productions.models

import app.umcu.api.database.dao.ProductionDao
import app.umcu.api.utils.ext.toSlug
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NextProduction(
	@SerialName("slug") override val slug: String,
	@SerialName("tmdb_id") override val tmdbId: Int,
	@SerialName("title") override val title: String,
	@SerialName("overview") override val overview: String?,
	@SerialName("release_date") override val releaseDate: Instant?,
	@SerialName("poster_url") override val posterUrl: String?,
	@SerialName("media_type") override val mediaType: String,
	val next: String?
) : SharedProduction {
	constructor(
		tmdbId: Int,
		title: String,
		overview: String? = null,
		releaseDate: Instant? = null,
		posterUrl: String? = null,
		mediaType: String,
		next: String? = null
	) : this(
		slug = title.toSlug(), tmdbId, title, overview, releaseDate, posterUrl, mediaType, next
	)

	companion object {
		fun toProduction(dao: ProductionDao, next: String? = null) =
			NextProduction(dao.tmdbId, dao.title, dao.overview, dao.releaseDate, dao.posterUrl, dao.mediaType, next)
	}
}
