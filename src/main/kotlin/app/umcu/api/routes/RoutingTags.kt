package app.umcu.api.routes

enum class RoutingTags(private val value: String) {
	PRODUCTIONS("productions");

	override fun toString(): String {
		return value
	}
}
