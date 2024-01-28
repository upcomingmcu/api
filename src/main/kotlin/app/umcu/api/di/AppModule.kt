package app.umcu.api.di

import app.umcu.api.data.productions.ProductionsDAOFacadeImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
	singleOf(::ProductionsDAOFacadeImpl)
}
