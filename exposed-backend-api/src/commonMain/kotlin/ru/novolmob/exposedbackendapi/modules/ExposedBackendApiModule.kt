package ru.novolmob.exposedbackendapi.modules

import org.koin.dsl.module

val exposedBackendApiModule = module {
    includes(mapperModule, repositoryModule)
}