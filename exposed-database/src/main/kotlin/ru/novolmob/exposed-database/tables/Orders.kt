package ru.novolmob.`exposed-database`.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.`exposed-database`.extensions.TableExtension.creationDate
import ru.novolmob.`exposed-database`.extensions.TableExtension.idColumn
import ru.novolmob.`exposed-database`.extensions.TableExtension.price
import ru.novolmob.core.models.ids.OrderId

object Orders: IdTable<OrderId>() {
    override val id: Column<EntityID<OrderId>> = idColumn(constructor = ::OrderId).entityId()
    override val primaryKey = PrimaryKey(id)

    val user = reference("user_id", Users)
    val point = reference("point_id", Points)
    val totalCost = price(name = "total_cost", precision = 10, scale = 2)
    val creationDate = creationDate()
}