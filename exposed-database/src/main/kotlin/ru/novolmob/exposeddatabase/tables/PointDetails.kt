package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.address
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationTime
import ru.novolmob.exposeddatabase.extensions.TableExtension.description
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.language
import ru.novolmob.exposeddatabase.extensions.TableExtension.schedule
import ru.novolmob.exposeddatabase.extensions.TableExtension.updateTime
import ru.novolmob.core.models.ids.PointDetailId

object PointDetails: IdTable<PointDetailId>() {
    override val id: Column<EntityID<PointDetailId>> = idColumn(constructor = ::PointDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    val point = reference("point_id", Points)
    val address = address()
    val schedule = schedule()
    val description = description()
    val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}