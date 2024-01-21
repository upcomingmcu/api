package app.umcu.api.routes

import app.umcu.api.models.Production
import app.umcu.api.models.Response
import app.umcu.api.models.sampleDataSet
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productionsRoute() {
	rateLimit {
		route("/v1/productions") {
			// allProductions
			route("") {
				allProductionsDoc()

				get {
					call.respond(Response(*Production.sampleDataSet))
				}
			}

			// productionBySlug
			route("/{slug}") {
				productionBySlugDoc()

				get {
					val slug = call.parameters["slug"]
					val foundProduction = Production.sampleDataSet.firstOrNull { it.slug.equals(slug, true) }
					if (foundProduction == null) throw NotFoundException()
					else call.respond(Response(foundProduction))
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
}

private fun Route.allProductionsDoc() {
	install(NotarizedRoute()) {
		tags = setOf(RoutingTags.PRODUCTIONS.toString())
		get = GetInfo.builder {
			summary("Get all productions.")
			description("Get a list of all previously released, upcoming, and to be announced productions.")
			operationId("allProductions")
			response {
				responseType<Response<Production>>()
				responseCode(HttpStatusCode.OK)
				description("All productions.")
			}
		}
	}
}

private fun Route.productionBySlugDoc() {
	install(NotarizedRoute()) {
		tags = setOf(RoutingTags.PRODUCTIONS.toString())
		get = GetInfo.builder {
			summary("Get a specific production by its slug.")
			description("Get a single production in the database by its unique slug identifier.")
			operationId("productionBySlug")
			parameters = listOf(
				Parameter(
					name = "slug",
					`in` = Parameter.Location.path,
					schema = TypeDefinition.STRING,
					description = "The slug of the production you want to get.",
					allowEmptyValue = false,
				)
			)
			response {
				responseType<Response<Production>>()
				responseCode(HttpStatusCode.OK)
				description("The production that matches the slug.")
			}
			canRespond {
				responseType<NotFoundException>()
				responseCode(HttpStatusCode.NotFound)
				description("No production by that slug could be found.")
			}
		}
	}
}

private fun Route.nextProductionDoc() {
	install(NotarizedRoute()) {
		tags = setOf(RoutingTags.PRODUCTIONS.toString())
		get = GetInfo.builder {
			summary("Get the next production to be released.")
			description("Get the next production to be released following the current, or provided, date.")
			operationId("nextProduction")
			parameters = listOf(
				Parameter(
					name = "date",
					`in` = Parameter.Location.query,
					schema = TypeDefinition.STRING,
					description = "The date to use when getting the next production.",
					allowEmptyValue = true,
				)
			)
			response {
				responseType<Response<Production>>()
				responseCode(HttpStatusCode.OK)
				description("The next production to be released.")
			}
			canRespond {
				responseType<NotFoundException>()
				responseCode(HttpStatusCode.NotFound)
				description("No production is upcoming.")
			}
		}
	}
}
