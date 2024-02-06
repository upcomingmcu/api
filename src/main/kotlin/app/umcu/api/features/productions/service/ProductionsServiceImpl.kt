package app.umcu.api.features.productions.service

import app.umcu.api.database.DatabaseSingleton
import app.umcu.api.database.dao.ProductionDao
import app.umcu.api.database.tables.ProductionsTable
import app.umcu.api.features.productions.models.Production
import org.koin.java.KoinJavaComponent.inject

class ProductionsServiceImpl : ProductionsService {
	private val databaseSingleton: DatabaseSingleton by inject(DatabaseSingleton::class.java)

	private fun daoToProduction(dao: ProductionDao) = Production(
		slug = dao.slug, tmdbId = dao.tmdbId, title = dao.title, releaseDate = dao.releaseDate
	)

	override suspend fun allProductions(): List<Production> = databaseSingleton.query {
		ProductionDao.all().map(::daoToProduction)
	}

	override suspend fun productionBySlug(slug: String): Production? = databaseSingleton.query {
		ProductionDao.find { ProductionsTable.slug eq slug }.map(::daoToProduction).firstOrNull()
	}
}
