package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.price
import ru.novolmob.core.models.ids.OrderId

object Orders: IdTable<OrderId>() {
    override val id: Column<EntityID<OrderId>> = idColumn(constructor = ::OrderId).entityId()
    override val primaryKey = PrimaryKey(id)

    val user = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
    val point = reference("point_id", Points, onDelete = ReferenceOption.CASCADE)
    val totalCost = price(name = "total_cost", precision = 10, scale = 2)
    val creationDate = creationTime()
}