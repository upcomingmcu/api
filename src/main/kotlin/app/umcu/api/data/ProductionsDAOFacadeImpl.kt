package app.umcu.api.data

import app.umcu.api.data.DatabaseSingleton.query
import app.umcu.api.models.Production

class ProductionsDAOFacadeImpl : ProductionsDAOFacade {
	private fun daoToProduction(dao: ProductionDao) = Production(
		slug = dao.slug, tmdbId = dao.tmdbId, title = dao.title, releaseDate = dao.releaseDate
	)

	override suspend fun allProductions(): List<Production> = query {
		ProductionDao.all().map(::daoToProduction)
	}

	override suspend fun productionBySlug(slug: String): Production? = query {
		ProductionDao.find { ProductionsTable.slug eq slug }.map(::daoToProduction).firstOrNull()
	}
}
