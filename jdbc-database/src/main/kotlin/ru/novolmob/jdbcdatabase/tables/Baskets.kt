package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.ids.BasketId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.get
import ru.novolmob.jdbcdatabase.extensions.TableExtension.amount
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object Baskets: IdTable<BasketId>() {

    override val id = idColumn(constructor = ::BasketId).primaryKey()
    val userId = reference("user_id", Users.id).onDeleteCascade()
    val deviceId = reference("device_id", Devices.id).onDeleteCascade()
    val amount = amount()
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
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
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: BasketId,
        userId: UserId? = null,
        deviceId: DeviceId? = null,
        amount: Amount? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        userId?.let { list.add(this.userId valueOf it) }
        deviceId?.let { list.add(this.deviceId valueOf it) }
        amount?.let { list.add(this.amount valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

    suspend fun getAmount(
        userId: UserId,
        deviceId: DeviceId,
    ): Amount = select(columns = arrayOf(this.amount), expression = (this.userId eq userId) and (this.deviceId eq deviceId)) {
        this get this@Baskets.amount
    }

    suspend fun deleteFor(
        userId: UserId
    ) = delete(expression = this.userId eq userId)
}