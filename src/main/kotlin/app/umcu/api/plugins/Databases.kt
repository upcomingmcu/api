package app.umcu.api.plugins

import app.umcu.api.database.DatabaseSingleton
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureDatabases() {
	val ds by inject<DatabaseSingleton>()
	ds.init()
}
