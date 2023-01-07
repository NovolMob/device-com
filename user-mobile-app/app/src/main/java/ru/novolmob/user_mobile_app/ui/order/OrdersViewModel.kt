package ru.novolmob.user_mobile_app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.models.OrderShortModel
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.user_mobile_app.services.IOrderService
import ru.novolmob.user_mobile_app.utils.ScreenNotification.Companion.notice

data class OrdersState(
    val orders: List<OrderShortModel> = emptyList(),
    val loading: Boolean = false
)

class OrdersViewModel(
    private val orderService: IOrderService
): ViewModel() {
    private val _state = MutableStateFlow(OrdersState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                orderService.orders.collectLatest { orders ->
                    _state.update {
                        it.copy(orders = orders.let { list ->
                            list.filter { it.active }.sortedByDescending { it.creationTime.dateTime } + list.filter { !it.active }.sortedByDescending { it.creationTime.dateTime }
                        })
                    }
                }
            }
        }
    }

    fun selectOrder(orderId: OrderId) {
        viewModelScope.launch {
            orderService.selectOrder(orderId).notice()
        }
    }

    fun cancelOrder(orderId: OrderId) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            orderService.cancelOrder(orderId).notice()
            _state.update { it.copy(loading = false) }
        }
    }

}