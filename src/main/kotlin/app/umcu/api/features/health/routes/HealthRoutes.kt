package app.umcu.api.features.health.routes

import app.umcu.api.OperationId
import app.umcu.api.RoutingTags
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.healthRoutes() {
	route("/health") {
		healthDoc()

		get {
			call.respond(HttpStatusCode.OK.toString())
		}
	}
}

fun Route.healthDoc() {
	install(NotarizedRoute()) {
		tags = setOf(RoutingTags.HEALTH.value)
		get = GetInfo.builder {
			summary("Check the health of the API.")
			description("") // todo
			operationId(OperationId.HEALTH.value)

			response {
				responseType<String>()
				responseCode(HttpStatusCode.OK)
				description("The API is in good health.")
			}
		}
	}
}
