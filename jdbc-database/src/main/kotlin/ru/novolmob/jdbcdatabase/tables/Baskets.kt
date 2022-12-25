package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.BasketId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.amount
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq

object Baskets: Table() {

    val id = idColumn(constructor = ::BasketId)
    val userId = reference("user_id", Users.id)
    val deviceId = reference("device_id", Devices.id)
    val amount = amount()
    val updateTime = updateTime()
    val creationTime = creationTime()

    fun insert(
        id: BasketId? = null,
        userId: UserId,
        deviceId: DeviceId,
        amount: Amount
    ) {
        val list = mutableListOf(
            this.userId valueOf userId,
            this.deviceId valueOf deviceId,
            this.amount valueOf amount,
        )
        id?.let { this.id valueOf it }

        insert(values = list.toTypedArray())
    }

    fun update(
        id: BasketId,
        userId: UserId? = null,
        deviceId: DeviceId? = null,
        amount: Amount? = null
    ) {
        val list = mutableListOf<ColumnValue<*>>()
        userId?.let { this.userId valueOf it }
        deviceId?.let { this.deviceId valueOf it }
        amount?.let { this.amount valueOf it }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }
}