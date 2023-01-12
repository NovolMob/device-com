package ru.novolmob.jdbcbackendapi.repositories

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.parTraverse
import arrow.fx.coroutines.parTraverseEither
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.cantCancelOrder
import ru.novolmob.backendapi.exceptions.failedToCreateCity
import ru.novolmob.backendapi.exceptions.notEnoughDevices
import ru.novolmob.backendapi.mappers.Mapper
import ru.novolmob.backendapi.models.OrderFullModel
import ru.novolmob.backendapi.models.OrderItemShortModel
import ru.novolmob.backendapi.models.OrderShortModel
import ru.novolmob.backendapi.repositories.IOrderRepository
import ru.novolmob.backendapi.repositories.IOrderToDeviceRepository
import ru.novolmob.backendapi.repositories.IOrderToStatusRepository
import ru.novolmob.backendapi.repositories.IPointRepository
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.functions.ConfirmOrderFunction
import ru.novolmob.jdbcdatabase.procedures.CancelOrderProcedure
import ru.novolmob.jdbcdatabase.views.OrderView
import java.sql.ResultSet

class OrderRepositoryImpl(
    val blankShortModelMapper: Mapper<ResultSet, OrderShortModel>,
    val blankFullModelMapper: Mapper<ResultSet, OrderFullModel>,
    val pointRepository: IPointRepository,
    val orderToStatusRepository: IOrderToStatusRepository,
    val orderToDeviceRepository: IOrderToDeviceRepository
): IOrderRepository {
    override suspend fun confirmOrder(
        userId: UserId,
        pointId: PointId,
        language: Language
    ): Either<AbstractBackendException, OrderShortModel> =
        either {
            pointRepository.get(pointId).bind()
            Either.catch( { notEnoughDevices(emptyList()) }) {
                ConfirmOrderFunction.call(userId, pointId, language) {
                    next()
                    val order = blankShortModelMapper(this).bind()
                    val lastStatus = orderToStatusRepository.getLastStatus(order.id, language).orNull()
                    val devices = orderToDeviceRepository.getDevices(order.id, language).orNull() ?: emptyList()
                    order.copy(
                        list = devices.parTraverse {
                            OrderItemShortModel(
                                deviceId = it.deviceId,
                                title = it.title,
                                amount = it.amount,
                                priceForOne = it.priceForOne
                            )
                        },
                        status = lastStatus?.title,
                        active = lastStatus?.active ?: true
                    ).right()
                }.bind()
            }.bind()
        }

    override suspend fun cancelOrder(orderId: OrderId): Either<AbstractBackendException, Boolean> =
        Either.catch( { cantCancelOrder(orderId) } ) {
            CancelOrderProcedure.call(orderId)
        }.flatMap { true.right() }

    override suspend fun getFull(
        orderId: OrderId,
        language: Language
    ): Either<AbstractBackendException, OrderFullModel> =
        either {
            OrderView.select(orderId, language) {
                next()
                val order = blankFullModelMapper(this).orNull() ?: return@select failedToCreateCity().left()
                val lastStatus = orderToStatusRepository.getLastStatus(order.id, language).orNull()
                val devices = orderToDeviceRepository.getDevices(order.id, language).orNull() ?: emptyList()
                order.copy(
                    list = devices,
                    lastStatus = lastStatus
                ).right()
            }.bind()
        }

    override suspend fun getOrdersFor(
        userId: UserId,
        language: Language
    ): Either<AbstractBackendException, List<OrderShortModel>> =
        either {
            OrderView.select(userId, language) {
                val result = mutableListOf<OrderShortModel>()
                while (next()) result.add(blankShortModelMapper(this).bind())
                result.parTraverseEither {
                    val lastStatus = orderToStatusRepository.getLastStatus(it.id, language).orNull()
                    val devices = orderToDeviceRepository.getDevices(it.id, language).orNull() ?: emptyList()
                    it.copy(
                        list = devices.parTraverse {
                           OrderItemShortModel(
                               deviceId = it.deviceId,
                               title = it.title,
                               amount = it.amount,
                               priceForOne = it.priceForOne
                           )
                        },
                        status = lastStatus?.title,
                        active = lastStatus?.active ?: true
                    ).right()
                }
            }.bind()
        }
}