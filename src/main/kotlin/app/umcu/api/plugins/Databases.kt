package app.umcu.api.plugins

import app.umcu.api.database.DatabaseSingleton
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureDatabases() {
	val databaseSingleton by inject<DatabaseSingleton>()
	databaseSingleton.init()
}
