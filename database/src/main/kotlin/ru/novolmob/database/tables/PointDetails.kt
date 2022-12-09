package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.address
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.description
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.schedule
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.database.models.ids.PointDetailId

object PointDetails: IdTable<PointDetailId>() {
    override val id: Column<EntityID<PointDetailId>> = idColumn(constructor = ::PointDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    val point = reference("point_id", Points)
    val address = address()
    val schedule = schedule()
    val description = description()
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}