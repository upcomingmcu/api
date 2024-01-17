package app.umcu.api.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.routing.*

fun Application.configureHTTP() {
	install(ForwardedHeaders)
	install(XForwardedHeaders)
	install(CachingHeaders) {
		options { _, outgoingContent ->
			when (outgoingContent.contentType?.withoutParameters()) {
				ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
				else -> null
			}
		}
	}
	install(ConditionalHeaders)
	install(CORS) {
		allowMethod(HttpMethod.Get)
		anyHost()
	}

	routing {
		openAPI(path = "openapi")
	}
}
