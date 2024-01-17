package app.umcu.api.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	install(StatusPages) {
		exception<Throwable> { call, cause ->
			call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
		}
		status(HttpStatusCode.NotFound) { call, code ->
			call.respondText(text = code.toString(), status = code)
		}
		status(HttpStatusCode.TooManyRequests) { call, code ->
			call.respondText(text = code.toString(), status = code)
		}
	}
	install(Resources)

	routing {
		get("/ping") {
			call.respond(HttpStatusCode.OK, HttpStatusCode.OK.toString())
		}

		staticResources("/static", "static")
	}
}
