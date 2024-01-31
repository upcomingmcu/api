package app.umcu.api.features.productions.data

import app.umcu.api.features.productions.models.Production

interface ProductionsDAOFacade {
	suspend fun allProductions(): List<Production>
	suspend fun productionBySlug(slug: String): Production?
}
