package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.price
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object Orders: IdTable<OrderId>() {

    override val id = idColumn(constructor = ::OrderId).primaryKey()
    val userId = reference("user_id", Users.id).onDeleteCascade()
    val pointId = reference("point_id", Points.id).onDeleteCascade()
    val totalCost = price(name = "total_cost", precision = 10, scale = 2)
    val creationTime = creationTime()

    suspend fun insert(
        id: OrderId? = null,
        userId: UserId,
        pointId: PointId,
        totalCost: Price,
    ) {
        val list = mutableListOf<ParameterValue<*>>(
            Orders.userId valueOf userId,
            Orders.pointId valueOf pointId,
            Orders.totalCost valueOf totalCost
        )
        id?.let { list.add(this.id valueOf id) }
        insert(*list.toTypedArray())
    }

    suspend fun update(
        id: OrderId,
        userId: UserId? = null,
        pointId: PointId? = null,
        totalCost: Price? = null,
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        userId?.let { list.add(Orders.userId valueOf it) }
        pointId?.let { list.add(Orders.pointId valueOf it) }
        totalCost?.let { list.add(Orders.totalCost valueOf it) }
        update(
            newValues = list.toTypedArray(),
            expression = Orders.id eq id
        )
    }

}