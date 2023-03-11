package ru.novolmob.devicecom.repositories

import arrow.core.Either
import io.ktor.client.*
import ru.novolmob.backend.ktorrouting.user.Orders
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.OrderFullModel
import ru.novolmob.backendapi.models.OrderShortModel
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.devicecom.utils.KtorUtil.delete
import ru.novolmob.devicecom.utils.KtorUtil.get
import ru.novolmob.devicecom.utils.KtorUtil.post

interface IOrderRepository: IRepository {
    suspend fun getAll(): Either<AbstractBackendException, List<OrderShortModel>>
    suspend fun get(id: OrderId): Either<AbstractBackendException, OrderFullModel>
    suspend fun confirmOrder(pointId: PointId): Either<AbstractBackendException, OrderShortModel>
    suspend fun cancelOrder(orderId: OrderId): Either<AbstractBackendException, Boolean>
}

class OrderRepositoryImpl(
    private val client: HttpClient
): IOrderRepository {
    override suspend fun getAll(): Either<AbstractBackendException, List<OrderShortModel>> =
        client.get(Orders())

    override suspend fun get(id: OrderId): Either<AbstractBackendException, OrderFullModel> =
        client.get(Orders.Id(orders = Orders(), id = id))

    override suspend fun confirmOrder(pointId: PointId): Either<AbstractBackendException, OrderShortModel> =
        client.post(Orders.Confirm(orders = Orders(), pointId = pointId))

    override suspend fun cancelOrder(orderId: OrderId): Either<AbstractBackendException, Boolean> =
        client.delete(Orders.Id(orders = Orders(), id = orderId))

}