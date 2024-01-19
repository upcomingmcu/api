package app.umcu.api.routes

import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productionsRoute() {
	rateLimit {
		route("/v1/productions") {
			get {
				call.respond("allProductions")
				// TODO ...
			}

			get("/{slug}") {
				val slug = call.parameters["slug"]
				call.respond("productionBySlug $slug")
				// TODO ...
			}

			get("/next") {
				call.respond("nextProduction")
				// TODO ...
			}
		}
	}
}
