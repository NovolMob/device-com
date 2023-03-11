package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Code
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.amount
import ru.novolmob.jdbcdatabase.extensions.TableExtension.code
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.price
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq

object Devices: IdTable<DeviceId>() {

    override val id = idColumn(constructor = ::DeviceId).primaryKey()
    val code = code()
    val typeId = reference("type_id", DeviceTypes.id).onDeleteCascade()
    val amount = amount()
    val price = price(precision = 10, scale = 2)
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
        id: DeviceId? = null,
        code: Code,
        typeId: DeviceTypeId,
        amount: Amount,
        price: Price
    ) {
        val list = mutableListOf(
            this.code valueOf code,
            this.typeId valueOf typeId,
            this.amount valueOf amount,
            this.price valueOf price
        )
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: DeviceId,
        code: Code? = null,
        typeId: DeviceTypeId? = null,
        amount: Amount? = null,
        price: Price? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()

        code?.let { list.add(this.code valueOf it) }
        typeId?.let { list.add(this.typeId valueOf it) }
        amount?.let { list.add(this.amount valueOf it) }
        price?.let { list.add(this.price valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}