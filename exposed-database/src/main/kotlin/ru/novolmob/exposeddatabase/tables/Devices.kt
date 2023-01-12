package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.exposeddatabase.utils.TableUtil.code
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.price
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.exposeddatabase.utils.TableUtil.amount

object Devices: IdTable<DeviceId>() {
    override val id: Column<EntityID<DeviceId>> = idColumn(constructor = ::DeviceId).entityId()
    override val primaryKey = PrimaryKey(id)

    val code = code()
    val type = reference("type_id", DeviceTypes, onDelete = ReferenceOption.CASCADE)
    val amount = amount()
    val price = price(precision = 10, scale = 2)
    val updateDate = updateTime()
    val creationDate = creationTime()
}