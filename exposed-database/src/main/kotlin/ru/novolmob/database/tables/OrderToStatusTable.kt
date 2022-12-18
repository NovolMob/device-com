package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.models.ids.OrderToStatusEntityId

object OrderToStatusTable: IdTable<OrderToStatusEntityId>() {
    override val id: Column<EntityID<OrderToStatusEntityId>> = idColumn(constructor = ::OrderToStatusEntityId).entityId()
    override val primaryKey = PrimaryKey(OrderStatusDetails.id)

    val order = reference("order_id", Orders)
    val orderStatus = reference("order_id", OrderStatuses)
    val worker = reference("worker_id", Workers)
    val creationDate = creationDate()

}