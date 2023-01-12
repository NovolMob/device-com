package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.exposeddatabase.utils.TableUtil.amount
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.price
import ru.novolmob.core.models.ids.OrderToDeviceEntityId

object OrderToDeviceTable: IdTable<OrderToDeviceEntityId>() {
    override val id: Column<EntityID<OrderToDeviceEntityId>> = idColumn(constructor = ::OrderToDeviceEntityId).entityId()
    override val primaryKey = PrimaryKey(id)

    val order = reference("order_id", Orders, onDelete = ReferenceOption.CASCADE)
    val device = reference("device_id", Devices, onDelete = ReferenceOption.CASCADE)
    val amount = amount()
    val priceForOne = price(name = "price_for_one", precision = 10, scale = 2)
}