package app.umcu.api.plugins

import app.umcu.api.di.appModule
import app.umcu.api.features.productions.di.productionsModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
	install(Koin) {
		modules(appModule, productionsModule)
		properties(mapOf("KtorApplicationEnvironment" to environment))
	}
}
