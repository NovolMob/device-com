package ru.novolmob.jdbcdatabase.tables.columns.types

interface IColumnType<T> {
    val databaseType: String

    fun toDbType(value: T): Any
    fun fromDbType(db: Any): T
}