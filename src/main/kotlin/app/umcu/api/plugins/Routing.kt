package app.umcu.api.plugins

import app.umcu.api.features.health.routes.healthRoutes
import app.umcu.api.features.productions.routes.productionsRoutes
import app.umcu.api.models.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	install(StatusPages) {
		exception<Throwable> { call, cause ->
			val code = HttpStatusCode.InternalServerError
			call.respond(code, ErrorResponse(code, cause.localizedMessage))
		}
		exception<NotFoundException> { call, cause ->
			val code = HttpStatusCode.NotFound
			call.respond(code, ErrorResponse(code, cause.localizedMessage))
		}
		exception<BadRequestException> { call, cause ->
			val code = HttpStatusCode.BadRequest
			call.respond(code, ErrorResponse(code, cause.localizedMessage))
		}

		status(HttpStatusCode.NotFound) { call, code -> call.respond(code, ErrorResponse(code)) }
		status(HttpStatusCode.TooManyRequests) { call, code -> call.respond(code, ErrorResponse(code)) }
	}

	install(Resources)

	routing {
		/**
		 * Define a rate limit of `n` requests per minute for all routes defined within this block.
		 */
		rateLimit {
			/**
			 * Prefix all routes with "/v1" (representing the current API version).
			 */
			route("/v1") {
				/**
				 * Define the routes for the health endpoint.
				 */
				healthRoutes()

				/**
				 * Define the routes for the productions endpoint.
				 */
				productionsRoutes()
			}
		}
	}
}
