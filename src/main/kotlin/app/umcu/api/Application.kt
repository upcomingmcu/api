@file:Suppress("unused")

package app.umcu.api

import app.umcu.api.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
	io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
	configureSerialization()
	configureDatabases()
	configureMonitoring()
	configureHTTP()
	configureRateLimit()
	configureMetrics()
	configureOpenApi()
	configureRouting()
}
