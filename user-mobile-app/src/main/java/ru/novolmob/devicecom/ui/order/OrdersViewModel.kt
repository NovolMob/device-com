package ru.novolmob.devicecom.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.models.OrderShortModel
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.devicecom.services.IOrderService
import ru.novolmob.devicecom.services.IProfileService
import ru.novolmob.devicecom.utils.ScreenNotification.Companion.notice
import java.util.*

data class OrdersState(
    val language: Locale = Locale.getDefault(),
    val orders: List<OrderShortModel> = emptyList(),
    val loading: Boolean = false
)

class OrdersViewModel(
    private val profileService: IProfileService,
    private val orderService: IOrderService
): ViewModel() {
    private val _state = MutableStateFlow(OrdersState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                profileService.profile.collectLatest { user ->
                    _state.update {
                        it.copy(
                            language = user?.language?.string?.let(::Locale) ?: Locale.getDefault()
                        )
                    }
                }
            }
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

    fun update() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            orderService.update().notice()
            _state.update { it.copy(loading = false) }
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