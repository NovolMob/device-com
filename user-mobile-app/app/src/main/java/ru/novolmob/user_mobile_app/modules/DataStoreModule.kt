package ru.novolmob.user_mobile_app.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.novolmob.user_mobile_app.datastore.ITokenDataStore
import ru.novolmob.user_mobile_app.datastore.TokenDataStore

val dataStoreModule = module {
    singleOf(::TokenDataStore).bind<ITokenDataStore>()
}