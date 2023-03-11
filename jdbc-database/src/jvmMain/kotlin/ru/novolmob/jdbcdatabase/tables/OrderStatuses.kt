package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.Column
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object OrderStatuses: IdTable<OrderStatusId>() {

    override val id: Column<OrderStatusId> = idColumn(constructor = ::OrderStatusId).primaryKey()
    val active = bool("active")
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
        id: OrderStatusId? = null,
        active: Boolean
    ) {
        val list = mutableListOf<ParameterValue<*>>(
            this.active valueOf active,
        )
        id?.let { list.add(this.id valueOf id) }
        insert(*list.toTypedArray())
    }

    suspend fun update(
        id: OrderStatusId,
        active: Boolean? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        active?.let { list.add(this.active valueOf it) }
        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}