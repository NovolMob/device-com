package ru.novolmob.jdbcdatabase.databases

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class DatabaseObject: KoinComponent {
    protected val database: Database by inject()

    abstract fun create()
}