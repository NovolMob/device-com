package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.user_mobile_app.models.BasketModel
import ru.novolmob.user_mobile_app.models.DeviceModel
import ru.novolmob.user_mobile_app.navigation.NavigationRoute

interface IBasketService: IService {
    val basket: StateFlow<BasketModel>

    suspend fun add(deviceModel: DeviceModel): Either<BackendException, Boolean>
    suspend fun remove(deviceId: DeviceId): Either<BackendException, Boolean>
    suspend fun setAmount(deviceId: DeviceId, amount: Int): Either<BackendException, Int>
    fun getOrNull(deviceId: DeviceId): DeviceModel?
}

class BasketServiceImpl: IBasketService {
    private val _basket = MutableStateFlow(BasketModel())
    override val basket: StateFlow<BasketModel> = _basket.asStateFlow()

    init {
        serviceScope.launch {
            launch { update() }
            launch {
                basket.collectLatest { NavigationRoute.Main.Basket.badge(it.list.size) }
            }
        }
    }


    override suspend fun update(): Either<BackendException, Unit> = Unit.right()

    override suspend fun add(deviceModel: DeviceModel): Either<BackendException, Boolean> {
        _basket.update { basket ->
            if (deviceModel.amountInBasketFlow.value <= 0) deviceModel.amountInBasket(1)
            val list = basket.list.toMutableList().apply {
                add(deviceModel)
                distinctBy { it.deviceId }
            }
            BasketModel(
                list = list,
                totalPrice = list.sumOf { it.price }
            )
        }
        return true.right()
    }

    override suspend fun remove(deviceId: DeviceId): Either<BackendException, Boolean> =
        getOrNull(deviceId)?.let { device ->
            _basket.update { basket ->
                val list = basket.list.toMutableList().apply {
                    removeIf {  it.deviceId == deviceId }
                    distinctBy { it.deviceId }
                }
                BasketModel(
                    list = list,
                    totalPrice = list.sumOf { it.price }
                ).also { device.amountInBasket(0) }
            }
            true.right()
        } ?: false.right()

    override suspend fun setAmount(deviceId: DeviceId, amount: Int): Either<BackendException, Int> {
        if (amount <= 0) remove(deviceId)
        else _basket.value.list.find { it.deviceId == deviceId }?.amountInBasket(amount)
        return amount.right()
    }

    override fun getOrNull(deviceId: DeviceId): DeviceModel? =
        _basket.value.list.find { it.deviceId == deviceId }

}