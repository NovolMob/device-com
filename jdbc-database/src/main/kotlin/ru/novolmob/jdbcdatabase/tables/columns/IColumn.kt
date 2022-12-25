package ru.novolmob.jdbcdatabase.tables.columns

import ru.novolmob.jdbcdatabase.tables.Table
import ru.novolmob.jdbcdatabase.tables.columns.types.IColumnType
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue

interface IColumn<T: Any> {
    val name: String
    val type: IColumnType<T>
    val table: Table

    fun buildCreationString(): String

    infix fun valueOf(value: T): ColumnValue<T>
}