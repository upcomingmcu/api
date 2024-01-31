package app.umcu.api.features.productions.routes

import app.umcu.api.OperationId
import app.umcu.api.RoutingTags
import app.umcu.api.features.productions.models.Production
import app.umcu.api.models.Response
import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

fun Route.allProductionsDoc() {
	install(NotarizedRoute()) {
		tags = setOf(RoutingTags.PRODUCTIONS.value)
		get = GetInfo.builder {
			summary("") // todo
			description("") // todo
			operationId(OperationId.ALL_PRODUCTIONS.value)

			response {
				responseType<Response<Production>>()
				responseCode(HttpStatusCode.OK)
				description("All productions.")
			}
		}
	}
}

fun Route.productionBySlugDoc() {
	install(NotarizedRoute()) {
		tags = setOf(RoutingTags.PRODUCTIONS.value)
		get = GetInfo.builder {
			summary("") // todo
			description("") // todo
			operationId(OperationId.PRODUCTION_BY_SLUG.value)

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
				description("No production by that slug exists.")
			}
		}
	}
}

fun Route.nextProductionDoc() {
	install(NotarizedRoute()) {
		tags = setOf(RoutingTags.PRODUCTIONS.value)
		get = GetInfo.builder {
			summary("") // todo
			description("") // todo
			operationId(OperationId.NEXT_PRODUCTION.value)

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
