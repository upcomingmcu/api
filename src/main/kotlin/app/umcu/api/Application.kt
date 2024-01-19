package app.umcu.api

import app.umcu.api.plugins.*
import io.bkbn.kompendium.core.attribute.KompendiumAttributes
import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.json.schema.KotlinXSchemaConfigurator
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.common.ExternalDocumentation
import io.bkbn.kompendium.oas.info.Contact
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.info.License
import io.bkbn.kompendium.oas.server.Server
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.URI

fun main(args: Array<String>) {
	io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
	configureSerialization()
	configureDatabases()
	configureMonitoring()
	configureHTTP()
	configureRateLimit()
	configureRouting()
	configureMetrics()

	install(NotarizedApplication()) {
		spec = OpenApiSpec(
			openapi = "3.1.0", info = Info(
				"UpcomingMCU API",
				"1.0.0",
				"The ultimate source for staying up-to-date with the MCU.",
				termsOfService = URI("https://umcu.app/about/terms"),
				contact = Contact("Sean O'Connor", URI("https://gitlab.com/seaneoo"), "api@umcu.app"),
				license = License("GNU Affero General Public License v3.0 or later", "AGPL-3.0-or-later")
			), servers = mutableListOf(
				Server(URI("https://api.umcu.app"), "Production server"),
				Server(URI("http://127.0.0.1:8080"), "Development server")
			), externalDocs = ExternalDocumentation(URI("https://gitlab.com/upcomingmcu/api"), "Source code")
		)
		schemaConfigurator = KotlinXSchemaConfigurator()
		openApiJson = {
			get("/openapi.json") {
				call.respond(this@get.application.attributes[KompendiumAttributes.openApiSpec])
			}
		}
	}
}
