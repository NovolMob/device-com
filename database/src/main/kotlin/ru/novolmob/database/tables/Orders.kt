package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.price
import ru.novolmob.database.models.ids.OrderId

object Orders: IdTable<OrderId>() {
    override val id: Column<EntityID<OrderId>> = idColumn(constructor = ::OrderId).entityId()
    override val primaryKey = PrimaryKey(id)

    val user = reference("user_id", Users)
    val point = reference("point_id", Points)
    val totalCost = price(name = "total_cost", precision = 10, scale = 2)
    val creationDate = creationDate()
}