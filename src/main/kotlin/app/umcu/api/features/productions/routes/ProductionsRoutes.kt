package app.umcu.api.features.productions.routes

import app.umcu.api.features.productions.service.ProductionsServiceImpl
import app.umcu.api.models.Response
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.productionsRoutes() {
	val service by inject<ProductionsServiceImpl>()

	route("/productions") {

		/**
		 * All productions.
		 */
		route("") {
			allProductionsDoc()

			get {
				call.respond(Response(service.allProductions()))
			}
		}

		/**
		 * Single production by slug.
		 */
		route("/{slug}") {
			productionBySlugDoc()

			get {
				val slug = call.parameters["slug"] ?: throw BadRequestException("Slug parameter is required.")
				val data = service.productionBySlug(slug) ?: throw NotFoundException()
				call.respond(Response(data))
			}
		}

		/**
		 * Next production.
		 */
		route("/next") {
			nextProductionDoc()

			get {
				// todo
				throw NotFoundException()
			}
		}
	}
}
