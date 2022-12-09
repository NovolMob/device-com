package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.database.models.ids.DeviceTypeId

object DeviceTypes: IdTable<DeviceTypeId>() {
    override val id: Column<EntityID<DeviceTypeId>> = idColumn(constructor = ::DeviceTypeId).entityId()
    override val primaryKey = PrimaryKey(id)

    val updateDate = updateDate()
    val creationDate = creationDate()
}