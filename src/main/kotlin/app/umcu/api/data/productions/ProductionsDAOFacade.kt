package app.umcu.api.data.productions

import app.umcu.api.models.Production

interface ProductionsDAOFacade {
	suspend fun allProductions(): List<Production>
	suspend fun productionBySlug(slug: String): Production?
}
