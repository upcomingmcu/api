package app.umcu.api.features.productions.service

import app.umcu.api.features.productions.model.Production

interface ProductionsService {
	fun findAllProductions(): List<Production>
	fun findProductionBySlug(slug: String): Production?
	fun findNextProduction(dateString: String? = null): Production?
}
