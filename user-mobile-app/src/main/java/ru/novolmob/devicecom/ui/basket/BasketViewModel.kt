package ru.novolmob.devicecom.ui.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.models.PointShortModel
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.devicecom.models.DeviceModel
import ru.novolmob.devicecom.services.IBasketService
import ru.novolmob.devicecom.services.IOrderService
import ru.novolmob.devicecom.services.IPointService
import ru.novolmob.devicecom.utils.ScreenNotification
import ru.novolmob.devicecom.utils.ScreenNotification.Companion.notice

data class BasketState(
    val list: List<DeviceModel> = emptyList(),
    val totalPriceString: String = "",
    val points: List<PointShortModel> = emptyList(),
    val loading: Boolean = false,
    val sending: Boolean = false
)

class BasketViewModel(
    private val basketService: IBasketService,
    private val pointService: IPointService,
    private val orderService: IOrderService
): ViewModel() {
    private val _state = MutableStateFlow(BasketState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                basketService.basket.collectLatest { basket ->
                    _state.update {
                        it.copy(
                            list = basket.list,
                            totalPriceString = basket.totalPriceString
                        )
                    }
                }
            }
            launch {
                pointService.getAllByUserCity().notice()?.let { points ->
                    _state.update {
                        it.copy(points = points)
                    }
                }
            }
        }
    }

    fun update() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            basketService.update().fold(
                ifLeft = { exception ->
                    ScreenNotification.push(exception)
                },
                ifRight = {
                }
            )
            _state.update { it.copy(loading = false) }
        }
    }

    fun setDeviceAmount(deviceId: DeviceId, amount: Int) {
        viewModelScope.launch {
            basketService.setAmount(deviceId, Amount(amount))
        }
    }

    fun deleteFromBasket(deviceId: DeviceId) {
        viewModelScope.launch {
            basketService.remove(deviceId)
        }
    }

    fun confirmOrder(pointId: PointId) {
        viewModelScope.launch {
            _state.update { it.copy(sending = true) }
            orderService.confirmOrder(pointId).fold(
                ifLeft = { exception ->
                    ScreenNotification.push(exception)
                    _state.update { it.copy(sending = false) }
                },
                ifRight = {
                    _state.update { it.copy(sending = false) }
                    update()
                }
            )
        }
    }
}