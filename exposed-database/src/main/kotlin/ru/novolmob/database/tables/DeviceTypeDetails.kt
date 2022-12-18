package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.description
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.title
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.DeviceTypeDetailId

object DeviceTypeDetails: IdTable<DeviceTypeDetailId>() {
    override val id: Column<EntityID<DeviceTypeDetailId>> = idColumn(constructor = ::DeviceTypeDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    val deviceType = reference("device_type_id", DeviceTypes)
    val title = title()
    val description = description()
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}