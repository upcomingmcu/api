package app.umcu.api.plugins

import app.umcu.api.di.appModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
	install(Koin) {
		modules(appModule)
	}
}
