package modules

import org.koin.dsl.module
import storages.TokenStorage

val storageModule = module {
    single {
        TokenStorage()
    }
}