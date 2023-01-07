package ru.novolmob.exposedbackendapi.exceptions

import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId

fun pointByIdNotFound(pointId: PointId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "Point with id $pointId not found!"
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