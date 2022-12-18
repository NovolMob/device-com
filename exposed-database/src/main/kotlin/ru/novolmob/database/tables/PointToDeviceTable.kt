package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.amount
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.database.models.ids.PointToDeviceEntityId

object PointToDeviceTable: IdTable<PointToDeviceEntityId>() {
    override val id: Column<EntityID<PointToDeviceEntityId>> = idColumn(constructor = ::PointToDeviceEntityId).entityId()
    override val primaryKey = PrimaryKey(id)

    val point = reference("point", Points)
    val device = reference("device", Devices)
    val amount = amount()
    val updateDate = updateDate()
    val creationDate = creationDate()
}