package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.ids.CityId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq

object Cities: Table() {

    val id = idColumn(constructor = ::CityId)
    val creationTime = creationTime()

    fun insert(
        id: CityId? = null
    ) {
        val list = mutableListOf<ColumnValue<*>>()
        id?.let { this.id valueOf it }

        insert(values = list.toTypedArray())
    }

    fun update(
        id: CityId
    ) {
        val list = mutableListOf<ColumnValue<*>>()

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }
}