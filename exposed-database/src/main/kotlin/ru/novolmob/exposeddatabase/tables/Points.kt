package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.exposeddatabase.extensions.TableExtension.city
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationDate
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.updateDate

object Points: IdTable<PointId>() {
    override val id: Column<EntityID<PointId>> = idColumn(constructor = ::PointId).entityId()
    override val primaryKey = PrimaryKey(id)

    val city = city()
    val updateDate = updateDate()
    val creationDate = creationDate()
}