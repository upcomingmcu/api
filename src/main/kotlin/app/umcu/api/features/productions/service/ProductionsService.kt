package app.umcu.api.features.productions.service

import app.umcu.api.features.productions.models.Production
import kotlinx.datetime.LocalDate

interface ProductionsService {
	suspend fun allProductions(): List<Production>
	suspend fun productionBySlug(slug: String): Production?
	suspend fun nextProduction(date: LocalDate? = null): Production?
}
