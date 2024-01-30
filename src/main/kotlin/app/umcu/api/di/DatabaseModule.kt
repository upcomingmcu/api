package app.umcu.api.di

import app.umcu.api.DatabaseSingleton
import io.ktor.server.application.*
import org.koin.dsl.module

val databaseModule = module {
	single {
		val applicationEnvironment: ApplicationEnvironment = getKoin().getProperty("KtorApplicationEnvironment")
			?: throw IllegalArgumentException("Koin could not find Ktor application environment.")
		DatabaseSingleton.getInstance(applicationEnvironment)
	}
}
