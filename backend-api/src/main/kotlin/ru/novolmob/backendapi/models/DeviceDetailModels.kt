package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.*
import ru.novolmob.database.models.ids.DeviceDetailId
import ru.novolmob.database.models.ids.DeviceId

@Serializable
data class DeviceDetailModel(
    val id: DeviceDetailId,
    val deviceId: DeviceId,
    val title: Title,
    val description: Description,
    val features: Features,
    val language: Language,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)
@Serializable
data class DeviceDetailCreateModel(
    val deviceId: DeviceId,
    val title: Title,
    val description: Description,
    val features: Features,
    val language: Language,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class DeviceDetailUpdateModel(
    val deviceId: DeviceId?,
    val title: Title?,
    val description: Description?,
    val features: Features?,
    val language: Language?,
    val updateDate: UpdateDate?,
    val creationDate: CreationDate?
)