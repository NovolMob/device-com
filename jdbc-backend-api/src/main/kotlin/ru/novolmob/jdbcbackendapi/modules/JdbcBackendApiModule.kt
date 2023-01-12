package ru.novolmob.jdbcbackendapi.modules

import org.koin.dsl.module
import ru.novolmob.jdbcdatabase.modules.jdbcDatabaseModule

val jdbcBackendApiModule = module {
    includes(jdbcDatabaseModule, mapperModule, repositoryModule)
}