package ru.novolmob.exposeddatabase.tables.details

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.description
import ru.novolmob.exposeddatabase.utils.TableUtil.features
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.language
import ru.novolmob.exposeddatabase.utils.TableUtil.title
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.exposeddatabase.tables.Devices

object DeviceDetails: DetailTable<DeviceDetailId, DeviceId>() {
    override val id: Column<EntityID<DeviceDetailId>> = idColumn(constructor = ::DeviceDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    override val parent = reference("device_id", Devices, onDelete = ReferenceOption.CASCADE)
    val title = title()
    val description = description()
    val features = features()
    override val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}