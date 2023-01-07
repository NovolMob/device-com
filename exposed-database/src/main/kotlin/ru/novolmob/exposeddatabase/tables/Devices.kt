package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.code
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationTime
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.price
import ru.novolmob.exposeddatabase.extensions.TableExtension.updateTime
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.exposeddatabase.extensions.TableExtension.amount

object Devices: IdTable<DeviceId>() {
    override val id: Column<EntityID<DeviceId>> = idColumn(constructor = ::DeviceId).entityId()
    override val primaryKey = PrimaryKey(id)

    val code = code()
    val type = reference("type_id", DeviceTypes)
    val amount = amount()
    val price = price(precision = 10, scale = 2)
    val updateDate = updateTime()
    val creationDate = creationTime()
}