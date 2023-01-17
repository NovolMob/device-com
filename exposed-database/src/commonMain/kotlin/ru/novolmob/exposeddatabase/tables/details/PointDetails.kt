package ru.novolmob.exposeddatabase.tables.details

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.exposeddatabase.utils.TableUtil.address
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.description
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.language
import ru.novolmob.exposeddatabase.utils.TableUtil.schedule
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.exposeddatabase.tables.Points

object PointDetails: DetailTable<PointDetailId, PointId>() {
    override val id: Column<EntityID<PointDetailId>> = idColumn(constructor = ::PointDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    override val parent = reference("point_id", Points, onDelete = ReferenceOption.CASCADE)
    val address = address()
    val schedule = schedule()
    val description = description()
    override val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}