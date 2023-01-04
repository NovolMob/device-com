package ru.novolmob.user_mobile_app.ui.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.user_mobile_app.models.DeviceModel
import ru.novolmob.user_mobile_app.services.IBasketService

data class BasketState(
    val list: List<DeviceModel> = emptyList(),
    val totalPriceString: String = ""
)

class BasketViewModel(
    private val basketService: IBasketService
): ViewModel() {
    private val _state = MutableStateFlow(BasketState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            basketService.basket.collectLatest { basket ->
                _state.update {
                    it.copy(list = basket.list, totalPriceString = basket.totalPriceString)
                }
            }
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

    fun confirmOrder() {

    }
}