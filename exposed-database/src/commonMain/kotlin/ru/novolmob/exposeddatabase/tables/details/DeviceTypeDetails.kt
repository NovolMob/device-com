package ru.novolmob.exposeddatabase.tables.details

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.description
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.language
import ru.novolmob.exposeddatabase.utils.TableUtil.title
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.exposeddatabase.tables.DeviceTypes

object DeviceTypeDetails: DetailTable<DeviceTypeDetailId, DeviceTypeId>() {
    override val id: Column<EntityID<DeviceTypeDetailId>> = idColumn(constructor = ::DeviceTypeDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    override val parent = reference("device_type_id", DeviceTypes, onDelete = ReferenceOption.CASCADE)
    val title = title()
    val description = description()
    override val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}