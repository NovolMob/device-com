package ru.novolmob.jdbcdatabase.tables.columns.values

import ru.novolmob.jdbcdatabase.tables.columns.types.IColumnType

class ColumnValue<T: Any>(
    val columnName: String,
    private val value: T,
    val type: IColumnType<T>
) {
    val dbValue: Any
        get() = type.toDbType(value)
}