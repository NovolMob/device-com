package ru.novolmob.exposedbackendapi.exceptions

import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId

fun cityByIdNotFound(id: CityId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "City with id $id not found!"
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