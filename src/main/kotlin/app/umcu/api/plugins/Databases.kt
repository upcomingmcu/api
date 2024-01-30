package app.umcu.api.plugins

import app.umcu.api.data.DatabaseSingleton
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureDatabases() {
	val ds: DatabaseSingleton by inject()
	ds.init()
}
