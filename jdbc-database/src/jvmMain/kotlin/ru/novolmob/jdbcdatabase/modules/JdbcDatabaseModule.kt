package ru.novolmob.jdbcdatabase.modules

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.novolmob.jdbcdatabase.databases.Database

val jdbcDatabaseModule = module {
    singleOf(::Database)
}