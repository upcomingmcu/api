package app.umcu.api.plugins

import app.umcu.api.di.databaseModule
import app.umcu.api.di.httpServiceModule
import app.umcu.api.features.productions.di.productionsModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
	install(Koin) {
		modules(databaseModule, httpServiceModule, productionsModule)
		properties(mapOf("KtorApplicationEnvironment" to environment))
	}
}
