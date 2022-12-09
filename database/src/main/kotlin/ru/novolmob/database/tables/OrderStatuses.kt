package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.models.ids.OrderStatusId

object OrderStatuses: IdTable<OrderStatusId>() {
    override val id: Column<EntityID<OrderStatusId>> = idColumn(constructor = ::OrderStatusId).entityId()
    override val primaryKey = PrimaryKey(id)

    val order = reference("order_id", Orders)
    val worker = reference("worker_id", Workers)
    val creationDate = creationDate()
}