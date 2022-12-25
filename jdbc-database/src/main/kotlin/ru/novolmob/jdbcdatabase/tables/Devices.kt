package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Code
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.code
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.price
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq

object Devices: Table() {

    val id = idColumn(constructor = ::DeviceId)
    val code = code()
    val typeId = reference("type_id", DeviceTypes.id)
    val price = price(precision = 10, scale = 2)
    val updateTime = updateTime()
    val creationTime = creationTime()

    fun insert(
        id: DeviceId? = null,
        code: Code,
        typeId: DeviceTypeId,
        price: Price
    ) {
        val list = mutableListOf(
            this.code valueOf code,
            this.typeId valueOf typeId,
            this.price valueOf price
        )
        id?.let { this.id valueOf it }

        insert(values = list.toTypedArray())
    }

    fun update(
        id: DeviceId,
        code: Code? = null,
        typeId: DeviceTypeId? = null,
        price: Price? = null
    ) {
        val list = mutableListOf<ColumnValue<*>>()

        code?.let { this.code valueOf it }
        typeId?.let { this.typeId valueOf it }
        price?.let { this.price valueOf it }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}