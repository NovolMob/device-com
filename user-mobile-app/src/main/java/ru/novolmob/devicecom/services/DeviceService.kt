package ru.novolmob.devicecom.services

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.BasketItemModel
import ru.novolmob.backendapi.models.DeviceFullModel
import ru.novolmob.backendapi.models.DeviceShortModel
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.devicecom.models.DeviceModel
import ru.novolmob.devicecom.repositories.IDeviceRepository

interface IDevicesService: IService {
    val devices: StateFlow<List<DeviceModel>>
    val device: StateFlow<Pair<DeviceModel, DeviceFullModel?>?>

    suspend fun setDeviceId(deviceId: DeviceId): Either<AbstractBackendException, Pair<DeviceModel, DeviceFullModel>>

    fun addAll(list: List<DeviceModel>)

    fun get(deviceId: DeviceId): DeviceModel?
    suspend fun getOrCreate(basketItem: BasketItemModel): DeviceModel
    suspend fun getOrCreate(deviceShortModel: DeviceShortModel): DeviceModel
    suspend fun getOrCreate(deviceFullModel: DeviceFullModel): DeviceModel
}

class DevicesServiceImpl(
    private val deviceRepository: IDeviceRepository
): IDevicesService {
    private val _devices = MutableStateFlow(emptyList<DeviceModel>())
    override val devices: StateFlow<List<DeviceModel>> = _devices.asStateFlow()

    private val _device = MutableStateFlow<Pair<DeviceModel, DeviceFullModel?>?>(null)
    override val device: StateFlow<Pair<DeviceModel, DeviceFullModel?>?> = _device.asStateFlow()

    override suspend fun setDeviceId(deviceId: DeviceId): Either<AbstractBackendException, Pair<DeviceModel, DeviceFullModel>> {
        if (deviceId != device.value?.first?.id) {
            _device.update { get(deviceId)?.let { it to null } }
        }
        return deviceRepository.get(deviceId).flatMap { model ->
            (getOrCreate(model) to model).let { pair ->
                _device.update { pair }
                pair.right()
            }
        }
    }

    override suspend fun update(): Either<AbstractBackendException, Any> =
        _device.value?.first?.id?.let { setDeviceId(it) } ?: Unit.right()

    override suspend fun clear() {
        _devices.update { emptyList() }
        _device.update { null }
    }

    override fun addAll(list: List<DeviceModel>) {
        _devices.update {
            it.toMutableList().apply {
                addAll(list)
                distinctBy { it.id }
            }
        }
    }

    private fun DeviceModel.add(): DeviceModel = apply { _devices.update { it + this } }

    override fun get(deviceId: DeviceId): DeviceModel? = devices.value.find { it.id == deviceId }

    override suspend fun getOrCreate(basketItem: BasketItemModel): DeviceModel =
        devices.value
            .find { it.id == basketItem.device.id }
            ?.apply {
                amountInBasket(basketItem.amount.int)
                title = basketItem.device.title.string
                description = basketItem.device.description.string
                price = basketItem.device.price.bigDecimal.toDouble()
            } ?:
        DeviceModel(
            id = basketItem.device.id,
            title = basketItem.device.title.string,
            description = basketItem.device.description.string,
            price = basketItem.device.price.bigDecimal.toDouble(),
        ).also { it.amountInBasket(basketItem.amount.int) }.add()

    override suspend fun getOrCreate(deviceShortModel: DeviceShortModel): DeviceModel =
        devices.value
                .find { it.id == deviceShortModel.id }?.apply {
                    title = deviceShortModel.title.string
                    description = deviceShortModel.description.string
                    price = deviceShortModel.price.bigDecimal.toDouble()
                } ?:
        DeviceModel(
            id = deviceShortModel.id,
            title = deviceShortModel.title.string,
            description = deviceShortModel.description.string,
            price = deviceShortModel.price.bigDecimal.toDouble(),
        ).add()

    override suspend fun getOrCreate(deviceFullModel: DeviceFullModel): DeviceModel =
        devices.value
                .find { it.id == deviceFullModel.id }?.apply {
                    title = deviceFullModel.detailModel.title.string
                    description = deviceFullModel.detailModel.description.string
                    price = deviceFullModel.price.bigDecimal.toDouble()
                } ?:
        DeviceModel(
            id = deviceFullModel.id,
            title = deviceFullModel.detailModel.title.string,
            description = deviceFullModel.detailModel.description.string,
            price = deviceFullModel.price.bigDecimal.toDouble(),
        ).add()

}