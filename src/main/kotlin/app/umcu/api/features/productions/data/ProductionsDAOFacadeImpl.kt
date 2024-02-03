package app.umcu.api.features.productions.data

import app.umcu.api.database.DatabaseSingleton
import app.umcu.api.database.dao.ProductionDao
import app.umcu.api.database.tables.ProductionsTable
import app.umcu.api.features.productions.models.Production
import org.koin.java.KoinJavaComponent.inject

class ProductionsDAOFacadeImpl : ProductionsDAOFacade {
	private val ds: DatabaseSingleton by inject(DatabaseSingleton::class.java)

	private fun daoToProduction(dao: ProductionDao) = Production(
		slug = dao.slug, tmdbId = dao.tmdbId, title = dao.title, releaseDate = dao.releaseDate
	)

	override suspend fun allProductions(): List<Production> = ds.query {
		ProductionDao.all().map(::daoToProduction)
	}

	override suspend fun productionBySlug(slug: String): Production? = ds.query {
		ProductionDao.find { ProductionsTable.slug eq slug }.map(::daoToProduction).firstOrNull()
	}
}
