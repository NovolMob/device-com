package ru.novolmob.`exposed-database`.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.`exposed-database`.extensions.TableExtension.creationDate
import ru.novolmob.`exposed-database`.extensions.TableExtension.idColumn
import ru.novolmob.`exposed-database`.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.DeviceTypeId

object DeviceTypes: IdTable<DeviceTypeId>() {
    override val id: Column<EntityID<DeviceTypeId>> = idColumn(constructor = ::DeviceTypeId).entityId()
    override val primaryKey = PrimaryKey(id)

    val updateDate = updateDate()
    val creationDate = creationDate()
}