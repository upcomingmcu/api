package app.umcu.api.database.dao

import app.umcu.api.database.tables.ProductionsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

@Suppress("unused")
class ProductionDao(id: EntityID<Int>) : IntEntity(id) {
	companion object : IntEntityClass<ProductionDao>(ProductionsTable)

	var slug by ProductionsTable.slug
	var tmdbId by ProductionsTable.tmdbId
	var title by ProductionsTable.title
	var overview by ProductionsTable.overview
	var releaseDate by ProductionsTable.releaseDate
	var posterUrl by ProductionsTable.posterUrl
	var mediaType by ProductionsTable.mediaType
}
