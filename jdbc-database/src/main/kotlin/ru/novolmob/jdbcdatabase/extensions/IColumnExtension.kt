package ru.novolmob.jdbcdatabase.extensions

import ru.novolmob.jdbcdatabase.tables.columns.IColumn

object IColumnExtension {

    fun IColumn<*>.tableColumnString() = "${table.tableName}.$name"

}