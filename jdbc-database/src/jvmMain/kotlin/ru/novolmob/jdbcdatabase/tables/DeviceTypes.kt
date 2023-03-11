package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object DeviceTypes: IdTable<DeviceTypeId>() {

    override val id = idColumn(constructor = ::DeviceTypeId).primaryKey()
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
        id: DeviceTypeId? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: DeviceTypeId
    ) {
        val list = mutableListOf<ParameterValue<*>>()

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}