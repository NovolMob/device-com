package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationTime
import ru.novolmob.exposeddatabase.extensions.TableExtension.description
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.language
import ru.novolmob.exposeddatabase.extensions.TableExtension.title
import ru.novolmob.exposeddatabase.extensions.TableExtension.updateTime
import ru.novolmob.core.models.ids.OrderStatusDetailId

object OrderStatusDetails: IdTable<OrderStatusDetailId>() {
    override val id: Column<EntityID<OrderStatusDetailId>> = idColumn(constructor = ::OrderStatusDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    val orderStatus = reference("order_status", OrderStatuses)
    val title = title()
    val description = description()
    val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}