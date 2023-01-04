package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.models.BasketFullModel
import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.user_mobile_app.models.BasketModel
import ru.novolmob.user_mobile_app.models.DeviceModel
import ru.novolmob.user_mobile_app.navigation.NavigationRoute
import ru.novolmob.user_mobile_app.repositories.IBasketRepository

interface IBasketService: IService {
    val basket: StateFlow<BasketModel>

    suspend fun add(deviceModel: DeviceModel): Either<BackendException, Unit>
    suspend fun remove(deviceId: DeviceId): Either<BackendException, Unit>
    suspend fun setAmount(deviceId: DeviceId, amount: Amount): Either<BackendException, Unit>
    fun getOrNull(deviceId: DeviceId): DeviceModel?
}

class BasketServiceImpl(
    private val basketRepository: IBasketRepository,
    private val devicesService: IDevicesService
): IBasketService {
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


    override suspend fun update(): Either<BackendException, BasketFullModel> =
        basketRepository.getBasket().flatMap { basketFullModel ->
            val list = basketFullModel.list.parTraverse {
                devicesService.getOrCreate(it)
            }
            _basket.update {
                BasketModel(
                    list = list,
                    totalPrice = basketFullModel.totalPrice.bigDecimal.toDouble()
                )
            }
            basketFullModel.right()
        }

    override suspend fun add(deviceModel: DeviceModel): Either<BackendException, Unit> =
        basketRepository.setInBasket(deviceId = deviceModel.id, amount = Amount(1)).flatMap { totalPrice ->
            _basket.update { old ->
                old.copy(
                    list = old.list.toMutableList().apply {
                        if (find { it.id == deviceModel.id } == null) {
                            add(deviceModel.also { it.amountInBasket(1) })
                        }
                    },
                    totalPrice = totalPrice.bigDecimal.toDouble()
                )
            }
            Unit.right()
        }

    override suspend fun remove(deviceId: DeviceId): Either<BackendException, Unit> =
        basketRepository.removeFromBasket(deviceId).flatMap { totalPrice ->
            _basket.update { old ->
                old.copy(
                    list = old.list.toMutableList().apply {
                        find { it.id == deviceId }?.amountInBasket(0)
                        removeIf { it.id == deviceId }
                    },
                    totalPrice = totalPrice.bigDecimal.toDouble()
                )
            }
            Unit.right()
        }

    override suspend fun setAmount(deviceId: DeviceId, amount: Amount): Either<BackendException, Unit> =
        (if (amount.int <= 0) remove(deviceId)
        else basketRepository.setInBasket(deviceId, amount).flatMap { totalPrice ->
            getOrNull(deviceId)?.amountInBasket(amount.int)
            _basket.update {
                it.copy(
                    totalPrice = totalPrice.bigDecimal.toDouble()
                )
            }
            Unit.right()
        })

    override fun getOrNull(deviceId: DeviceId): DeviceModel? =
        _basket.value.list.find { it.id == deviceId }

}