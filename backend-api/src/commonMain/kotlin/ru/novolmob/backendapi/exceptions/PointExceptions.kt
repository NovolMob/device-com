package ru.novolmob.backendapi.exceptions

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId

fun pointByIdNotFound(pointId: PointId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "Point with id $pointId not found!"
    )

fun failedToCreatePoint() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.UNKNOWN,
        message = "Failed to create point!"
    )

fun pointDetailByIdNotFound(pointDetailId: PointDetailId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "PointDetail with id $pointDetailId not found!"
    )

fun pointDetailByPointIdAndLanguageNotFound(pointId: PointId, language: Language) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "PointDetail with language $language for point $pointId not found!"
    )

fun failedToCreatePointDetail() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.UNKNOWN,
        message = "Failed to create point !"
    )