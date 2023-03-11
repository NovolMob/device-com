package ru.novolmob.exposedbackendapi.repositories.details

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.jetbrains.exposed.sql.ResultRow
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.orderStatusByIdNotFound
import ru.novolmob.backendapi.exceptions.orderStatusDetailByDeviceIdAndLanguageNotFound
import ru.novolmob.backendapi.exceptions.orderStatusDetailByIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusDetailCreateModel
import ru.novolmob.backendapi.models.OrderStatusDetailModel
import ru.novolmob.backendapi.models.OrderStatusDetailUpdateModel
import ru.novolmob.backendapi.repositories.IOrderStatusDetailRepository
import ru.novolmob.core.models.UpdateTime
import ru.novolmob.core.models.ids.OrderStatusDetailId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.exposeddatabase.entities.OrderStatus
import ru.novolmob.exposeddatabase.entities.details.OrderStatusDetail

class OrderStatusDetailRepositoryImpl(
    mapper: Mapper<OrderStatusDetail, OrderStatusDetailModel>,
    resultRowMapper: Mapper<ResultRow, OrderStatusDetailModel>
): IOrderStatusDetailRepository,
    AbstractDetailRepository<OrderStatusDetailId, OrderStatusDetail.Companion, OrderStatusDetail, OrderStatus, OrderStatusId, OrderStatusDetailModel, OrderStatusDetailCreateModel, OrderStatusDetailUpdateModel>(
        OrderStatusDetail.Companion, mapper, resultRowMapper, ::orderStatusDetailByIdNotFound, ::orderStatusDetailByDeviceIdAndLanguageNotFound
    ) {

    override fun OrderStatusDetail.Companion.new(createModel: OrderStatusDetailCreateModel): Either<AbstractBackendException, OrderStatusDetail> {
        val orderStatus = OrderStatus.findById(createModel.parentId)
            ?: return orderStatusByIdNotFound(createModel.parentId).left()
        return new {
            this.parent = orderStatus
            this.title = createModel.title
            this.description = createModel.description
            this.language = language
        }.right()
    }

    override fun OrderStatusDetail.applyC(createModel: OrderStatusDetailCreateModel): Either<AbstractBackendException, OrderStatusDetail> {
        val orderStatus = OrderStatus.findById(createModel.parentId)
            ?: return orderStatusByIdNotFound(createModel.parentId).left()
        return apply {
            this.parent = orderStatus
            this.title = createModel.title
            this.description = createModel.description
            this.language = language
            this.updateDate = UpdateTime.now()
        }.right()
    }

    override fun OrderStatusDetail.applyU(updateModel: OrderStatusDetailUpdateModel): Either<AbstractBackendException, OrderStatusDetail> {
        val orderStatus = updateModel.parentId?.let {
            OrderStatus.findById(it) ?: return orderStatusByIdNotFound(it).left()
        }
        return apply {
            orderStatus?.let { this.parent = it }
            updateModel.title?.let { this.title = it }
            updateModel.description?.let { this.description = it }
            updateModel.language?.let { this.language = it }
            this.updateDate = UpdateTime.now()
        }.right()
    }

}