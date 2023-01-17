package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.CityId

@Serializable
data class CityModel(
    val id: CityId
)

@Serializable
class CityCreateModel()

@Serializable
class CityUpdateModel()

@Serializable
data class CityFullModel(
    val id: CityId,
    val detail: CityDetailModel
)

@Serializable
data class CityShortModel(
    val id: CityId,
    val title: Title
)