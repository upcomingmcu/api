package app.umcu.api.features.productions.di

import app.umcu.api.features.productions.service.ProductionsServiceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val productionsModule = module {
	singleOf(::ProductionsServiceImpl)
}
