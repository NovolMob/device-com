package ru.novolmob.exposeddatabase.entities.details

import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.OrderStatusDetailId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.exposeddatabase.entities.OrderStatus
import ru.novolmob.exposeddatabase.tables.details.OrderStatusDetails

class OrderStatusDetail(id: EntityID<OrderStatusDetailId>) : DetailEntity<OrderStatusDetailId, OrderStatus, OrderStatusId>(id) {
    companion object: DetailEntityClass<OrderStatusDetailId, OrderStatusDetail, OrderStatus, OrderStatusId>(OrderStatusDetails)

    override var parent by OrderStatus referencedOn OrderStatusDetails.parent
    var title by OrderStatusDetails.title
    var description by OrderStatusDetails.description
    override var language by OrderStatusDetails.language
    var updateDate by OrderStatusDetails.updateDate
    var creationDate by OrderStatusDetails.creationDate
}