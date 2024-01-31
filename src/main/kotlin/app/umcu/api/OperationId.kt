package app.umcu.api

enum class OperationId(val value: String) {
	HEALTH("health"),
	ALL_PRODUCTIONS("allProductions"),
	PRODUCTION_BY_SLUG("productionBySlug"),
	NEXT_PRODUCTION("nextProduction")
}
