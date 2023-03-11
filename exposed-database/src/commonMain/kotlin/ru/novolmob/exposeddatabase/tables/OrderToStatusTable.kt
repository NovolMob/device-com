package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.core.models.ids.OrderToStatusEntityId
import ru.novolmob.exposeddatabase.tables.details.OrderStatusDetails

object OrderToStatusTable: IdTable<OrderToStatusEntityId>() {
    override val id: Column<EntityID<OrderToStatusEntityId>> = idColumn(constructor = ::OrderToStatusEntityId).entityId()
    override val primaryKey = PrimaryKey(OrderStatusDetails.id)

    val order = reference("order_id", Orders, onDelete = ReferenceOption.CASCADE)
    val orderStatus = reference("order_status_id", OrderStatuses, onDelete = ReferenceOption.CASCADE)
    val worker = reference("worker_id", Workers, onDelete = ReferenceOption.CASCADE)
    val creationDate = creationTime()

}