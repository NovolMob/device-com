package ru.novolmob.exposedbackendapi.exceptions

import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.*

fun orderByIdNotFound(orderId: OrderId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "Order with id $orderId not found!"
    )

fun cantCancelOrder(orderId: OrderId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Can`t cancel order $orderId!"
    )

fun orderStatusByIdNotFound(orderStatusId: OrderStatusId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderStatus with id $orderStatusId not found!"
    )

fun orderStatusDetailByIdNotFound(orderStatusDetailId: OrderStatusDetailId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderStatusDetail with id $orderStatusDetailId not found!"
    )

fun orderStatusDetailByDeviceIdAndLanguageNotFound(orderStatusId: OrderStatusId, language: Language) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderStatusDetail with language $language for order status $orderStatusId not found!"
    )

fun orderToStatusEntityByIdNotFound(orderToStatusEntityId: OrderToStatusEntityId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderToDeviceEntity for order $orderToStatusEntityId not found!"
    )

fun orderStatusByOrderIdNotFound(orderId: OrderId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderStatus for order $orderId not found!"
    )

fun orderToDeviceEntityByIdNotFound(orderToDeviceEntityId: OrderToDeviceEntityId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderToDeviceEntity with id $orderToDeviceEntityId not found!"
    )

fun notEnoughDevices(list: List<DeviceId>) =
    AbstractBackendException.PayloadBackendException(
        code = BackendExceptionCode.NOT_ENOUGH_DEVICES,
        message = "Not enough devices [${list.joinToString()}]!"
    ).payload(list)