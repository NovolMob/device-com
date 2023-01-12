package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime

object Points: IdTable<PointId>() {
    override val id: Column<EntityID<PointId>> = idColumn(constructor = ::PointId).entityId()
    override val primaryKey = PrimaryKey(id)

    val city = reference("city_id", Cities, onDelete = ReferenceOption.CASCADE)
    val updateDate = updateTime()
    val creationDate = creationTime()
}