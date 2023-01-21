package ru.novolmob.devicecom.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.novolmob.devicecom.datastore.ITokenDataStore
import ru.novolmob.devicecom.datastore.TokenDataStore

val dataStoreModule = module {
    singleOf(::TokenDataStore).bind<ITokenDataStore>()
}