package app.umcu.api.features.productions.routes

import app.umcu.api.features.productions.data.ProductionsDAOFacadeImpl
import app.umcu.api.models.Response
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.configureProductionsRoute() {
	val facade by inject<ProductionsDAOFacadeImpl>()

	route("/v1/productions") {
		// allProductions
		route("") {
			allProductionsDoc()

			get {
				call.respond(Response(facade.allProductions()))
			}
		}

		// productionBySlug
		route("/{slug}") {
			productionBySlugDoc()

			get {
				val slug = call.parameters["slug"] ?: throw BadRequestException("Slug parameter is required.")
				val data = facade.productionBySlug(slug) ?: throw NotFoundException()
				call.respond(Response(data))
			}
		}

		// nextProduction
		route("/next") {
			nextProductionDoc()

			get {
				throw NotFoundException()
			}
		}
	}
}
