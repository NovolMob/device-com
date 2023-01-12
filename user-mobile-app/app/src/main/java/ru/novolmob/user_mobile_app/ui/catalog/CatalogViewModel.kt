package ru.novolmob.user_mobile_app.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.user_mobile_app.models.DeviceModel
import ru.novolmob.user_mobile_app.services.IBasketService
import ru.novolmob.user_mobile_app.services.ICatalogService
import ru.novolmob.user_mobile_app.services.IDevicesService

data class CatalogState(
    val searchString: String = "",
    val deviceTypeId: DeviceTypeId? = null,
    val page: Int = 0,
    val catalog: List<DeviceModel> = emptyList(),
    val amountOfPage: Int = 0,
    val searching: Boolean = false
)

class CatalogViewModel(
    private val catalogService: ICatalogService,
    private val basketService: IBasketService,
    private val devicesService: IDevicesService,
): ViewModel() {
    private val _state = MutableStateFlow(
        CatalogState(
            searchString = catalogService.sample.value.searchString,
            deviceTypeId = catalogService.sample.value.deviceTypeId,
            page = catalogService.catalog.value.page,
            catalog = catalogService.catalog.value.list,
            amountOfPage = catalogService.catalog.value.amountOfPage
        )
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                catalogService.catalog.collectLatest { catalog ->
                    _state.update {
                        it.copy(
                            page = catalog.page,
                            catalog = catalog.list,
                            amountOfPage = catalog.amountOfPage
                        )
                    }
                }
            }
        }
    }

    fun searchString(searchString: String) {
        _state.update { it.copy(searchString = searchString) }
    }

    fun selectedPage(selectedPage: Int) {
        _state.update { it.copy(page = selectedPage) }
        viewModelScope.launch { catalogService.page(selectedPage) }
    }


    fun search() {
        viewModelScope.launch {
            _state.update { it.copy(searching = true) }
            catalogService.searchString(_state.value.searchString)
            _state.update { it.copy(searching = false) }
        }
    }

    fun addToBasket(deviceModel: DeviceModel) {
        viewModelScope.launch {
            basketService.add(deviceModel)
        }
    }

    fun setDeviceAmount(deviceId: DeviceId, amount: Int) {
        viewModelScope.launch {
            basketService.setAmount(deviceId, Amount(amount))
        }
    }

    fun openDevice(deviceId: DeviceId) {
        viewModelScope.launch {
            devicesService.setDeviceId(deviceId)
        }
    }

}