package ru.novolmob.user_mobile_app.ui.device

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.user_mobile_app.services.IBasketService
import ru.novolmob.user_mobile_app.services.IDevicesService

data class DeviceState(
    val deviceId: DeviceId? = null,
    val imageFlow: StateFlow<ImageBitmap?> = MutableStateFlow(null).asStateFlow(),
    val amountInBasketFlow: StateFlow<Int> = MutableStateFlow(0).asStateFlow(),
    val title: String = "",
    val article: String = "",
    val description: String = "",
    val type: String = "",
    val price: Double = 0.0,
    val features: Map<String, String> = emptyMap(),
    val points: List<String> = emptyList(),
) {
    val priceString: String
        get() = "$price ₽"
}

class DeviceViewModel(
    private val deviceService: IDevicesService,
    private val basketService: IBasketService
): ViewModel() {
    private val _state = MutableStateFlow(DeviceState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                deviceService.device.collectLatest { pair ->
                    pair?.let { (deviceModel, fullModel) ->
                        _state.update { old ->
                            old.copy(
                                deviceId = deviceModel.id,
                                imageFlow = deviceModel.imageFlow,
                                amountInBasketFlow = deviceModel.amountInBasketFlow,
                                title = fullModel?.detailModel?.title?.string ?: deviceModel.title,
                                article = fullModel?.article?.string ?: "",
                                description = fullModel?.detailModel?.description?.string ?: deviceModel.description,
                                type = fullModel?.type?.detail?.title?.string ?: "",
                                price = fullModel?.price?.bigDecimal?.toDouble() ?: deviceModel.price,
                                features = fullModel?.detailModel?.features?.map ?: emptyMap(),
                                points = fullModel?.points?.map { it.pointDetail.address.string } ?: emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    fun addToBasket() {
        deviceService.device.value?.first?.let {
            viewModelScope.launch {
                basketService.add(it)
            }
        }
    }

    fun setAmountInBasket(amount: Int) {
        _state.value.deviceId?.let {
            viewModelScope.launch {
                basketService.setAmount(it, Amount(amount))
            }
        }
    }

    fun deleteFromBasket() {
        _state.value.deviceId?.let {
            viewModelScope.launch {
                basketService.remove(it)
            }
        }
    }


}