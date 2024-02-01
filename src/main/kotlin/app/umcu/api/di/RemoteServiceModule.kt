package app.umcu.api.di

import app.umcu.api.remote.RemoteServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.module

val remoteServiceModule = module {
	single {
		val applicationEnvironment: ApplicationEnvironment = getKoin().getProperty("KtorApplicationEnvironment")
			?: throw IllegalArgumentException("Koin could not find Ktor application environment.")
		val config = applicationEnvironment.config

		RemoteServiceImpl(tmdbApiKey = config.property("tmdb_api_key").getString())
	}
}
