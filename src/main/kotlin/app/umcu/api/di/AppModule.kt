package app.umcu.api.di

import app.umcu.api.DatabaseSingleton
import app.umcu.api.remote.HttpService
import app.umcu.api.remote.RemoteServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin

val appModule = module {
	val applicationEnvironment: ApplicationEnvironment = getKoin().getProperty("KtorApplicationEnvironment")
		?: throw IllegalArgumentException("Koin could not find Ktor application environment.")

	single {
		DatabaseSingleton.getInstance(applicationEnvironment)
	}

	single { HttpService() }

	single { RemoteServiceImpl(applicationEnvironment.config.property("tmdb_read_access_token").getString()) }
}
