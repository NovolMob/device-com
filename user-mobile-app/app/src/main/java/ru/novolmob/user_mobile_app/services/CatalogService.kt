package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.user_mobile_app.models.CatalogModel
import ru.novolmob.user_mobile_app.models.SearchSampleModel
import ru.novolmob.user_mobile_app.models.DeviceModel
import java.util.*

interface ICatalogService: IService {
    val catalog: StateFlow<CatalogModel>
    val sample: StateFlow<SearchSampleModel>

    suspend fun page(page: Int)
    suspend fun searchString(searchString: String)
    suspend fun searchByDeviceType(deviceTypeId: DeviceTypeId)

}

class CatalogServiceImpl(
    private val basketService: IBasketService
): ICatalogService {
    private val _catalog = MutableStateFlow(
        CatalogModel(
            list = List(8) {
                DeviceModel(
                    deviceId = DeviceId(UUID.randomUUID()),
                    title = "Название устройства",
                    description = "Очень большое описание устройства.",
                    price = 3999.0,
                    amountInBasket = 0
                )
            },
            amountOfPage = 1
        )
    )
    override val catalog: StateFlow<CatalogModel> = _catalog.asStateFlow()

    private val _sample = MutableStateFlow(SearchSampleModel(pageSize = 50))
    override val sample: StateFlow<SearchSampleModel> = _sample.asStateFlow()

    init {
        serviceScope.launch {
            launch { update() }
            launch {
                basketService.basket.collectLatest { basket ->
                    val basketMap = basket.list.associateBy { it.deviceId }
                    _catalog.update {
                        CatalogModel(
                            list = it.list.toMutableList().map { device -> basketMap[device.deviceId] ?: device },
                            amountOfPage = it.amountOfPage
                        )
                    }
                }
            }
        }
    }

    override suspend fun update(): Either<BackendException, Unit> = Unit.right()

    override suspend fun page(page: Int) =
        _sample.update { it.copy(page = page) }

    override suspend fun searchString(searchString: String) =
        _sample.update { it.copy(searchString = searchString) }

    override suspend fun searchByDeviceType(deviceTypeId: DeviceTypeId) =
        _sample.update { it.copy(deviceTypeId = deviceTypeId) }

}