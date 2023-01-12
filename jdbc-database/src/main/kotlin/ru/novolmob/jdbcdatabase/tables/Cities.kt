package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.ids.CityId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object Cities: IdTable<CityId>() {

    override val id = idColumn(constructor = ::CityId).primaryKey()
    val creationTime = creationTime()

    suspend fun insert(
        id: CityId? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: CityId
    ) {
        val list = mutableListOf<ParameterValue<*>>()

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }
}