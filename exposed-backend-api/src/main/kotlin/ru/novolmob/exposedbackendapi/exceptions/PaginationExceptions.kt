package ru.novolmob.exposedbackendapi.exceptions

import org.jetbrains.exposed.sql.Table
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode

fun tableDontHaveColumnException(table: Table, columnName: String) =
    BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "${table.tableName} does not have a column called $columnName. It have ${table.columns.map { it.name }}."
    )

fun badSortOrderException(name: String) =
    BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "SortOrder named $name does not exist."
    )