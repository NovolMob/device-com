package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderItemModel
import ru.novolmob.backendapi.models.OrderToDeviceEntityCreateModel
import ru.novolmob.backendapi.repositories.IOrderToDeviceRepository
import ru.novolmob.backendapi.utils.EitherUtil.backend
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.jdbcbackendapi.utils.ResultSetUtil.list
import ru.novolmob.jdbcdatabase.tables.OrderToDeviceTable
import ru.novolmob.jdbcdatabase.views.OrderToDeviceView
import java.sql.ResultSet

class OrderToDeviceRepositoryImpl(
    val mapper: Mapper<ResultSet, OrderItemModel>
): IOrderToDeviceRepository {
    override suspend fun getDevices(
        orderId: OrderId,
        language: Language
    ): Either<AbstractBackendException, List<OrderItemModel>> =
        OrderToDeviceView.select(orderId, language) { list(mapper) }

    override suspend fun post(createModel: OrderToDeviceEntityCreateModel): Either<AbstractBackendException, Unit> =
        Either.backend {
            OrderToDeviceTable.insert(
                orderId = createModel.orderId,
                deviceId = createModel.deviceId,
                amount = createModel.amount,
                priceForOne = createModel.priceForOne
            )
        }
}