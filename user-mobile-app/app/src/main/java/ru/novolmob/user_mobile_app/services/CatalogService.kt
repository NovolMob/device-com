package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.user_mobile_app.models.CatalogModel
import ru.novolmob.user_mobile_app.models.SearchSampleModel
import ru.novolmob.user_mobile_app.repositories.ICatalogRepository
import ru.novolmob.user_mobile_app.utils.ScreenNotification.Companion.notice

interface ICatalogService: IService {
    val catalog: StateFlow<CatalogModel>
    val sample: StateFlow<SearchSampleModel>

    suspend fun page(page: Int)
    suspend fun searchString(searchString: String)
    suspend fun searchByDeviceType(deviceTypeId: DeviceTypeId)

}

class CatalogServiceImpl(
    private val catalogRepository: ICatalogRepository,
    private val devicesService: IDevicesService
): ICatalogService {
    private val _catalog = MutableStateFlow(CatalogModel())
    override val catalog: StateFlow<CatalogModel> = _catalog.asStateFlow()

    private val _sample = MutableStateFlow(SearchSampleModel(pageSize = 20))
    override val sample: StateFlow<SearchSampleModel> = _sample.asStateFlow()

    init {
        serviceScope.launch {
            launch {
                update().notice()
            }
        }
    }

    override suspend fun update(): Either<AbstractBackendException, CatalogModel> =
        catalogRepository.getCatalog(
            _sample.value.toCatalog()
        ).flatMap { catalogModel ->
            CatalogModel(
                page = catalogModel.page,
                pageSize = catalogModel.pageSize,
                list = catalogModel.devices.parTraverse {
                    devicesService.getOrCreate(it)
                },
                amountOfPage = catalogModel.amountOfPages
            ).also { newCatalogModel ->
                _catalog.update { newCatalogModel }
            }.right()
        }

    override suspend fun page(page: Int) {
        _catalog.update { it.copy(page = page, list = emptyList()) }
        _sample.update { it.copy(page = page) }
        update()
    }

    override suspend fun searchString(searchString: String) {
        _catalog.update { it.copy(list = emptyList(), page = 0, amountOfPage = 0) }
        _sample.update { it.copy(searchString = searchString, page = 0) }
        update()
    }

    override suspend fun searchByDeviceType(deviceTypeId: DeviceTypeId) {
        _catalog.update { it.copy(list = emptyList(), page = 0, amountOfPage = 0) }
        _sample.update { it.copy(deviceTypeId = deviceTypeId, page = 0) }
        update()
    }

}