package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.core.models.ids.OrderToStatusEntityId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.Column
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object OrderToStatusTable: IdTable<OrderToStatusEntityId>() {

    override val id: Column<OrderToStatusEntityId> = idColumn(constructor = ::OrderToStatusEntityId)
    val orderId = reference("order_id", Orders.id).onDeleteCascade().primaryKey()
    val orderStatusId = reference("order_status_id", OrderStatuses.id).onDeleteCascade().primaryKey()
    val workerId = reference("worker", Workers.id).onDeleteCascade()
    val creationTime = creationTime()

    suspend fun insert(
        id: OrderToStatusEntityId? = null,
        orderId: OrderId,
        orderStatusId: OrderStatusId,
        workerId: WorkerId
    ) {
        val list = mutableListOf<ParameterValue<*>>(
            this.orderId valueOf orderId,
            this.orderStatusId valueOf orderStatusId,
            this.workerId valueOf workerId
        )
        id?.let { list.add(this.id valueOf id) }
        insert(*list.toTypedArray())
    }

    suspend fun update(
        id: OrderToStatusEntityId,
        orderId: OrderId? = null,
        orderStatusId: OrderStatusId? = null,
        workerId: WorkerId? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        orderId?.let { list.add(this.orderId valueOf it) }
        orderStatusId?.let { list.add(this.orderStatusId valueOf it) }
        workerId?.let { list.add(this.workerId valueOf it) }
        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

    suspend fun delete(
        orderId: OrderId,
        orderStatusId: OrderStatusId
    ) = delete(expression = (this.orderId eq orderId) and (this.orderStatusId eq orderStatusId))

}