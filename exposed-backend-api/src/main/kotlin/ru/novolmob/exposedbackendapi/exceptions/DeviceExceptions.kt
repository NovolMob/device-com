package ru.novolmob.exposedbackendapi.exceptions

import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.*

fun deviceByIdNotFound(deviceId: DeviceId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "Device with id $deviceId not found!"
    )

fun deviceTypeByIdNotFound(deviceTypeId: DeviceTypeId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "DeviceType with id $deviceTypeId not found!"
    )

fun deviceDetailByIdNotFound(deviceDetailId: DeviceDetailId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "DeviceDetail with id $deviceDetailId not found!"
    )

fun deviceTypeDetailByIdNotFound(deviceTypeDetailId: DeviceTypeDetailId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "DeviceTypeDetail with id $deviceTypeDetailId not found!"
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