package app.umcu.api.features.productions.models

import app.umcu.api.database.dao.ProductionDao
import app.umcu.api.utils.ext.toSlug
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Production(
	override val slug: String,
	@SerialName("tmdb_id") override val tmdbId: Int,
	override val title: String,
	@SerialName("release_date") override val releaseDate: Instant?
) : SharedProduction {
	constructor(tmdbId: Int, title: String, releaseDate: Instant? = null) : this(
		slug = title.toSlug(), tmdbId, title, releaseDate
	)

	companion object {
		fun toProduction(dao: ProductionDao) = Production(dao.tmdbId, dao.title, dao.releaseDate)
	}
}
