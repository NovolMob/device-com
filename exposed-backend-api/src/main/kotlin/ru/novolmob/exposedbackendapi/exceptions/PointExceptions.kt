package ru.novolmob.exposedbackendapi.exceptions

import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.PointToDeviceEntityId

fun pointByIdNotFound(pointId: PointId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "Point with id $pointId not found!"
    )

fun pointDetailByIdNotFound(pointDetailId: PointDetailId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "PointDetail with id $pointDetailId not found!"
    )

fun pointToDeviceEntityByIdNotFound(pointToDeviceEntityId: PointToDeviceEntityId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "PointToDeviceEntity with id $pointToDeviceEntityId not found!"
    )

fun pointDetailByPointIdAndLanguageNotFound(pointId: PointId, language: Language) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "PointDetail with language $language for point $pointId not found!"
    )