package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationTime
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.updateTime
import ru.novolmob.core.models.ids.DeviceTypeId

object DeviceTypes: IdTable<DeviceTypeId>() {
    override val id: Column<EntityID<DeviceTypeId>> = idColumn(constructor = ::DeviceTypeId).entityId()
    override val primaryKey = PrimaryKey(id)

    val updateDate = updateTime()
    val creationDate = creationTime()
}