package app.umcu.api.di

import app.umcu.api.database.DatabaseSingleton
import app.umcu.api.remote.DataCollectionServiceImpl
import app.umcu.api.remote.HttpService
import app.umcu.api.remote.RemoteServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.module

val appModule = module {
	single {
		val applicationEnvironment: ApplicationEnvironment = getKoin().getProperty("KtorApplicationEnvironment")
			?: throw IllegalArgumentException("Koin could not find Ktor application environment.")
		applicationEnvironment
	}

	single {
		DatabaseSingleton.getInstance(get<ApplicationEnvironment>().config)
	}

	single { HttpService() }

	single {
		RemoteServiceImpl(get<ApplicationEnvironment>().config.property("tmdb_read_access_token").getString())
	}

	single { DataCollectionServiceImpl() }
}
