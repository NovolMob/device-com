package ru.novolmob.jdbcdatabase.tables.parameters

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

interface IParameter<T: Any> {
    val name: String
    val type: IParameterType<T>
    val databaseObject: DatabaseObject

    fun buildCreationString(): String

    infix fun valueOf(value: T?): ParameterValue<T>
}