package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.OrderFullModel
import ru.novolmob.backendapi.models.OrderShortModel
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.user_mobile_app.navigation.NavigationRoute
import ru.novolmob.user_mobile_app.repositories.IOrderRepository
import ru.novolmob.user_mobile_app.utils.ScreenNotification.Companion.notice

interface IOrderService: IService {
    val orders: StateFlow<List<OrderShortModel>>
    val order: StateFlow<OrderFullModel?>

    fun getOrNull(orderId: OrderId): OrderShortModel?
    suspend fun selectOrder(orderId: OrderId): Either<AbstractBackendException, OrderFullModel>
    suspend fun confirmOrder(pointId: PointId): Either<AbstractBackendException, OrderShortModel>
    suspend fun cancelOrder(orderId: OrderId): Either<AbstractBackendException, Boolean>
}

class OrderServiceImpl(
    private val orderRepository: IOrderRepository
): IOrderService {
    private val _orders = MutableStateFlow(emptyList<OrderShortModel>())
    override val orders: StateFlow<List<OrderShortModel>> = _orders.asStateFlow()

    private val _order = MutableStateFlow<OrderFullModel?>(null)
    override val order: StateFlow<OrderFullModel?> = _order.asStateFlow()

    init {
        serviceScope.launch {
            launch { update().notice() }
            launch {
                _orders.collectLatest {
                    NavigationRoute.Main.Orders.badge(it.filter { it.active }.size)
                }
            }
        }
    }

    override suspend fun update(): Either<AbstractBackendException, List<OrderShortModel>> =
        orderRepository.getAll().flatMap { orders ->
            _orders.update { orders }
            orders.right()
        }

    override suspend fun clear() {
        _orders.update { emptyList() }
        _order.update { null }
    }

    override suspend fun selectOrder(orderId: OrderId): Either<AbstractBackendException, OrderFullModel> =
        orderRepository.get(orderId).flatMap { orderFullModel ->
            _order.update { orderFullModel }
            orderFullModel.right()
        }

    override fun getOrNull(orderId: OrderId): OrderShortModel? =
        orders.value.find { it.id == orderId }

    override suspend fun confirmOrder(pointId: PointId): Either<AbstractBackendException, OrderShortModel> =
        orderRepository.confirmOrder(pointId).flatMap { orderFullModel ->
            _orders.update { it + orderFullModel }
            orderFullModel.right()
        }

    override suspend fun cancelOrder(orderId: OrderId): Either<AbstractBackendException, Boolean> =
        orderRepository.cancelOrder(orderId).flatMap { answer ->
            if (answer) _orders.update { it.toMutableList().apply { removeIf { it.id == orderId } } }
            answer.right()
        }
}