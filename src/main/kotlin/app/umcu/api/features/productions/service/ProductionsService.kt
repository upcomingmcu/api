package app.umcu.api.features.productions.service

import app.umcu.api.features.productions.models.Production

interface ProductionsService {
	suspend fun allProductions(): List<Production>
	suspend fun productionBySlug(slug: String): Production?
}
