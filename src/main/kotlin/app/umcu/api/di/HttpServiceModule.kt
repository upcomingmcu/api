package app.umcu.api.di

import app.umcu.api.remote.HttpService
import org.koin.dsl.module

val httpServiceModule = module {
	single {
		HttpService()
	}
}
