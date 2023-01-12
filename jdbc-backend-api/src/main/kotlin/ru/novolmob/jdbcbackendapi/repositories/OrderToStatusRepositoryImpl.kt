package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.orderStatusByOrderIdNotFound
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderStatusFullModel
import ru.novolmob.backendapi.models.OrderStatusShortModel
import ru.novolmob.backendapi.models.OrderToStatusEntityCreateModel
import ru.novolmob.backendapi.repositories.IOrderToStatusRepository
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.OrderStatusId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.fold
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.list
import ru.novolmob.jdbcdatabase.functions.GettingLastStatusFunction
import ru.novolmob.jdbcdatabase.tables.OrderToStatusTable
import ru.novolmob.jdbcdatabase.views.OrderToStatusView
import java.sql.ResultSet

class OrderToStatusRepositoryImpl(
    val fullModelMapper: Mapper<ResultSet, OrderStatusFullModel>,
    val shortModelMapper: Mapper<ResultSet, OrderStatusShortModel>
): IOrderToStatusRepository {
    override suspend fun getStatuses(
        orderId: OrderId,
        language: Language
    ): Either<AbstractBackendException, List<OrderStatusFullModel>> =
        OrderToStatusView.select(orderId, language) { list(fullModelMapper) }

    override suspend fun getLastStatus(
        orderId: OrderId,
        language: Language
    ): Either<AbstractBackendException, OrderStatusShortModel> =
        GettingLastStatusFunction.call(orderId, language) { fold(ifEmpty = { orderStatusByOrderIdNotFound(orderId) }, shortModelMapper::invoke) }

    override suspend fun post(createModel: OrderToStatusEntityCreateModel): Either<AbstractBackendException, Unit> =
        Either.backend {
            OrderToStatusTable.insert(
                orderId = createModel.orderId,
                orderStatusId = createModel.status,
                workerId = createModel.workerId
            )
        }

    override suspend fun delete(
        orderId: OrderId,
        orderStatusId: OrderStatusId
    ): Either<AbstractBackendException, Boolean> =
        (OrderToStatusTable.delete(orderId, orderStatusId) > 0).right()
}