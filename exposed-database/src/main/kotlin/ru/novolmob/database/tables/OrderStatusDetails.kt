package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.description
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.title
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.database.models.ids.OrderStatusDetailId

object OrderStatusDetails: IdTable<OrderStatusDetailId>() {
    override val id: Column<EntityID<OrderStatusDetailId>> = idColumn(constructor = ::OrderStatusDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    val orderStatus = reference("order_status", OrderStatuses)
    val title = title()
    val description = description()
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}