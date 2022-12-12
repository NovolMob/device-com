package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.amount
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.price
import ru.novolmob.core.models.ids.OrderToDeviceEntityId

object OrderToDeviceTable: IdTable<OrderToDeviceEntityId>() {
    override val id: Column<EntityID<OrderToDeviceEntityId>> = idColumn(constructor = ::OrderToDeviceEntityId).entityId()
    override val primaryKey = PrimaryKey(id)

    val order = reference("order_id", Orders)
    val device = reference("device_id", Devices)
    val amount = amount()
    val priceForOne = price(name = "price_for_one", precision = 10, scale = 2)
}