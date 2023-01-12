package ru.novolmob.jdbcdatabase.tables.parameters.types

interface IParameterType<T> {
    val databaseType: String
    val databaseTypeId: Int

    fun toDbType(value: T): Any
    fun fromDbType(db: Any): T
}