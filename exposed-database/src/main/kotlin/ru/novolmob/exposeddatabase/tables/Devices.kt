package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.code
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationDate
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.price
import ru.novolmob.exposeddatabase.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.DeviceId

object Devices: IdTable<DeviceId>() {
    override val id: Column<EntityID<DeviceId>> = idColumn(constructor = ::DeviceId).entityId()
    override val primaryKey = PrimaryKey(id)

    val code = code()
    val type = reference("type_id", DeviceTypes)
    val price = price(precision = 10, scale = 2)
    val updateDate = updateDate()
    val creationDate = creationDate()
}