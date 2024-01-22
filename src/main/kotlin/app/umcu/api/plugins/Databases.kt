package app.umcu.api.plugins

import app.umcu.api.data.DatabaseSingleton
import io.ktor.server.application.*

fun Application.configureDatabases() {
	val embedded = environment.config.property("ktor.development").getString().toBoolean()
	DatabaseSingleton.init(environment, embedded)
}
