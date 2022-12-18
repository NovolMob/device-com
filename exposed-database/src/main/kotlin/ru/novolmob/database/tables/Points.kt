package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.PointId

object Points: IdTable<PointId>() {
    override val id: Column<EntityID<PointId>> = idColumn(constructor = ::PointId).entityId()
    override val primaryKey = PrimaryKey(id)

    val updateDate = updateDate()
    val creationDate = creationDate()
}