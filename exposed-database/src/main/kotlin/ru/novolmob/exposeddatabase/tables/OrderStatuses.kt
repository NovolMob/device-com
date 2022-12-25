package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationTime
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.core.models.ids.OrderStatusId

object OrderStatuses: IdTable<OrderStatusId>() {
    override val id: Column<EntityID<OrderStatusId>> = idColumn(constructor = ::OrderStatusId).entityId()
    override val primaryKey = PrimaryKey(id)

    val active = bool("active")
    val creationDate = creationTime()
}