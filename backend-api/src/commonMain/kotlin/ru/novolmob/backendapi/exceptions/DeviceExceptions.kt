package ru.novolmob.backendapi.exceptions

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId

fun deviceByIdNotFound(deviceId: DeviceId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "Device with id $deviceId not found!"
    )

fun failedToCreateDevice() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.UNKNOWN,
        message = "Failed to create device!"
    )

fun deviceTypeByIdNotFound(deviceTypeId: DeviceTypeId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "DeviceType with id $deviceTypeId not found!"
    )

fun failedToCreateDeviceType() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.UNKNOWN,
        message = "Failed to create device type!"
    )

fun deviceDetailByIdNotFound(deviceDetailId: DeviceDetailId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "DeviceDetail with id $deviceDetailId not found!"
    )

fun failedToCreateDeviceDetail() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.UNKNOWN,
        message = "Failed to create device detail!"
    )

fun deviceTypeDetailByIdNotFound(deviceTypeDetailId: DeviceTypeDetailId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "DeviceTypeDetail with id $deviceTypeDetailId not found!"
    )

fun failedToCreateDeviceTypeDetail() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.UNKNOWN,
        message = "Failed to create device type detail!"
    )

fun deviceDetailByDeviceIdAndLanguageNotFound(deviceId: DeviceId, language: Language) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "DeviceDetail with language $language for device $deviceId not found!"
    )

fun deviceTypeDetailByDeviceIdAndLanguageNotFound(deviceTypeId: DeviceTypeId, language: Language) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "DeviceTypeDetail with language $language for device type $deviceTypeId not found!"
    )