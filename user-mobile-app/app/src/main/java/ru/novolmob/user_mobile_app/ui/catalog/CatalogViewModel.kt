package ru.novolmob.user_mobile_app.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.user_mobile_app.models.CatalogModel
import ru.novolmob.user_mobile_app.models.SearchSampleModel
import ru.novolmob.user_mobile_app.models.DeviceModel
import ru.novolmob.user_mobile_app.services.IBasketService
import ru.novolmob.user_mobile_app.services.ICatalogService

data class CatalogState(
    val catalog: CatalogModel = CatalogModel(),
    val searchSample: SearchSampleModel
)

class CatalogViewModel(
    private val catalogService: ICatalogService,
    private val basketService: IBasketService
): ViewModel() {
    private val _state = MutableStateFlow(CatalogState(searchSample = catalogService.sample.value))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                catalogService.sample.collectLatest { sample ->
                    _state.update { it.copy(searchSample = sample) }
                }
            }
            launch {
                catalogService.catalog.collectLatest { catalog ->
                    _state.update { it.copy(catalog = catalog) }
                }
            }
        }
    }

    fun searchString(searchString: String) {
        viewModelScope.launch { catalogService.searchString(searchString) }
    }

    fun selectedPage(selectedPage: Int) {
        viewModelScope.launch { catalogService.page(selectedPage) }
    }


    fun search() {
        viewModelScope.launch { catalogService.update() }
    }

    fun addToBasket(deviceModel: DeviceModel) {
        viewModelScope.launch {
            basketService.add(deviceModel)
        }
    }

    fun setDeviceAmount(deviceId: DeviceId, amount: Int) {
        viewModelScope.launch {
            basketService.setAmount(deviceId, amount)
        }
    }

}