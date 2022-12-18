package ru.novolmob.`exposed-database`.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.`exposed-database`.extensions.TableExtension.creationDate
import ru.novolmob.`exposed-database`.extensions.TableExtension.description
import ru.novolmob.`exposed-database`.extensions.TableExtension.features
import ru.novolmob.`exposed-database`.extensions.TableExtension.idColumn
import ru.novolmob.`exposed-database`.extensions.TableExtension.language
import ru.novolmob.`exposed-database`.extensions.TableExtension.title
import ru.novolmob.`exposed-database`.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.DeviceDetailId

object DeviceDetails: IdTable<DeviceDetailId>() {
    override val id: Column<EntityID<DeviceDetailId>> = idColumn(constructor = ::DeviceDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    val device = reference("device_id", Devices)
    val title = title()
    val description = description()
    val features = features()
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}