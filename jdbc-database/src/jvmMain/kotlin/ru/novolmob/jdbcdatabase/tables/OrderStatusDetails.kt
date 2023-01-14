package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.OrderStatusDetailId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.description
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.language
import ru.novolmob.jdbcdatabase.extensions.TableExtension.title
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.Column
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object OrderStatusDetails: IdTable<OrderStatusDetailId>() {

    override val id: Column<OrderStatusDetailId> = idColumn(constructor = ::OrderStatusDetailId).primaryKey()
    val orderStatusId = reference("order_status_id", OrderStatuses.id).onDeleteCascade()
    val title = title()
    val description = description()
    val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
        id: OrderStatusDetailId? = null,
        orderStatusId: OrderStatusId,
        title: Title,
        description: Description,
        language: Language,
    ) {
        val list = mutableListOf<ParameterValue<*>>(
            this.orderStatusId valueOf orderStatusId,
            this.title valueOf title,
            this.description valueOf description,
            this.language valueOf language
        )
        id?.let { list.add(this.id valueOf id) }
        insert(*list.toTypedArray())
    }

    suspend fun update(
        id: OrderStatusDetailId,
        orderStatusId: OrderStatusId? = null,
        title: Title? = null,
        description: Description? = null,
        language: Language? = null,
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        orderStatusId?.let { list.add(this.orderStatusId valueOf it) }
        title?.let { list.add(this.title valueOf it) }
        description?.let { list.add(this.description valueOf it) }
        language?.let { list.add(this.language valueOf it) }
        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}