package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId

@Serializable
data class CityDetailModel(
    val id: CityDetailId,
    val cityId: CityId,
    val title: Title,
    val language: Language
)

@Serializable
data class CityDetailCreateModel(
    val cityId: CityId,
    val title: Title,
    val language: Language
)

@Serializable
data class CityDetailUpdateModel(
    val cityId: CityId? = null,
    val title: Title? = null,
    val language: Language? = null
)