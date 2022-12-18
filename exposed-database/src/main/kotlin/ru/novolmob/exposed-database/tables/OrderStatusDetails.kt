package ru.novolmob.`exposed-database`.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.`exposed-database`.extensions.TableExtension.creationDate
import ru.novolmob.`exposed-database`.extensions.TableExtension.description
import ru.novolmob.`exposed-database`.extensions.TableExtension.idColumn
import ru.novolmob.`exposed-database`.extensions.TableExtension.language
import ru.novolmob.`exposed-database`.extensions.TableExtension.title
import ru.novolmob.`exposed-database`.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.OrderStatusDetailId

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