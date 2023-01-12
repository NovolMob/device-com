package ru.novolmob.backendapi.exceptions

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId

fun cityByIdNotFound(id: CityId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "City with id $id not found!"
    )

fun failedToCreateCity() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.UNKNOWN,
        message = "Failed to create city!"
    )

fun cityDetailByIdNotFound(cityDetailId: CityDetailId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "CityDetail with id $cityDetailId not found!"
    )

fun cityDetailByCityIdAndLanguageNotFound(cityId: CityId, language: Language) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "CityDetail with language $language for city $cityId not found!"
    )