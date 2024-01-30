package app.umcu.api.plugins

import app.umcu.api.di.appModule
import app.umcu.api.di.databaseModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
	install(Koin) {
		modules(databaseModule, appModule)
		properties(mapOf("KtorApplicationEnvironment" to environment))
	}
}
