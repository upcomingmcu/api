package app.umcu.api.data.productions

import app.umcu.api.data.DatabaseSingleton
import app.umcu.api.models.Production

class ProductionsDAOFacadeImpl : ProductionsDAOFacade {
	private fun daoToProduction(dao: ProductionDao) = Production(
		slug = dao.slug, tmdbId = dao.tmdbId, title = dao.title, releaseDate = dao.releaseDate
	)

	override suspend fun allProductions(): List<Production> = DatabaseSingleton.instance.query {
		ProductionDao.all().map(::daoToProduction)
	}

	override suspend fun productionBySlug(slug: String): Production? = DatabaseSingleton.instance.query {
		ProductionDao.find { ProductionsTable.slug eq slug }.map(::daoToProduction).firstOrNull()
	}
}
