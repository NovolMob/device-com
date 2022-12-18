package ru.novolmob.backend.exceptions

import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.*

fun orderByIdNotFound(orderId: OrderId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "Order with id $orderId not found!"
    )

fun orderStatusByIdNotFound(orderStatusId: OrderStatusId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderStatus with id $orderStatusId not found!"
    )

fun orderStatusDetailByIdNotFound(orderStatusDetailId: OrderStatusDetailId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderStatusDetail with id $orderStatusDetailId not found!"
    )

fun orderStatusDetailByDeviceIdAndLanguageNotFound(orderStatusId: OrderStatusId, language: Language) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderStatusDetail with language $language for order status $orderStatusId not found!"
    )

fun orderToStatusEntityByIdNotFound(orderToStatusEntityId: OrderToStatusEntityId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderToDeviceEntity for order $orderToStatusEntityId not found!"
    )

fun orderStatusByOrderIdNotFound(orderId: OrderId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderStatus for order $orderId not found!"
    )

fun orderToDeviceEntityByIdNotFound(orderToDeviceEntityId: OrderToDeviceEntityId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "OrderToDeviceEntity with id $orderToDeviceEntityId not found!"
    )