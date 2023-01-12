package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderToDeviceEntityId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.amount
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.price
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.Column
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object OrderToDeviceTable: IdTable<OrderToDeviceEntityId>() {

    override val id: Column<OrderToDeviceEntityId> = idColumn(constructor = ::OrderToDeviceEntityId)
    val orderId = reference("order_id", Orders.id).onDeleteCascade().primaryKey()
    val deviceId = reference("device_id", Devices.id).onDeleteCascade().primaryKey()
    val amount = amount()
    val priceForOne = price("price_for_one", 10, 2)

    suspend fun insert(
        id: OrderToDeviceEntityId? = null,
        orderId: OrderId,
        deviceId: DeviceId,
        amount: Amount,
        priceForOne: Price
    ) {
        val list = mutableListOf<ParameterValue<*>>(
            this.orderId valueOf orderId,
            this.deviceId valueOf deviceId,
            this.amount valueOf amount,
            this.priceForOne valueOf priceForOne
        )
        id?.let { list.add(this.id valueOf id) }
        insert(*list.toTypedArray())
    }

    suspend fun update(
        id: OrderToDeviceEntityId,
        orderId: OrderId? = null,
        deviceId: DeviceId? = null,
        amount: Amount? = null,
        priceForOne: Price? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        orderId?.let { list.add(this.orderId valueOf it) }
        deviceId?.let { list.add(this.deviceId valueOf it) }
        amount?.let { list.add(this.amount valueOf it) }
        priceForOne?.let { list.add(this.priceForOne valueOf it) }
        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}