package ru.novolmob.jdbcdatabase.databases

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DatabaseObject: KoinComponent {
    abstract val name: String
    val database: Database by inject()

    abstract suspend fun create()
    override fun toString(): String = name
}