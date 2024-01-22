package app.umcu.api.plugins

import app.umcu.api.models.ErrorResponse
import app.umcu.api.routes.productionsRoute
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
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
		rateLimit {
			get("/ping") {
				call.respond(HttpStatusCode.OK, HttpStatusCode.OK.toString())
			}
		}

		productionsRoute()

		staticResources("/static", "static")
	}
}
