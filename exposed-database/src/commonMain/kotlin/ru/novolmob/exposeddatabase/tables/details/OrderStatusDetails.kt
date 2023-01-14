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
import ru.novolmob.core.models.ids.OrderStatusDetailId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.exposeddatabase.tables.OrderStatuses

object OrderStatusDetails: DetailTable<OrderStatusDetailId, OrderStatusId>() {
    override val id: Column<EntityID<OrderStatusDetailId>> = idColumn(constructor = ::OrderStatusDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    override val parent = reference("order_status", OrderStatuses, onDelete = ReferenceOption.CASCADE)
    val title = title()
    val description = description()
    override val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}