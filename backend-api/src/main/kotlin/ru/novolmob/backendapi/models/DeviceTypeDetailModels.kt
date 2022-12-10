package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.*
import ru.novolmob.database.models.ids.DeviceTypeDetailId
import ru.novolmob.database.models.ids.DeviceTypeId

@Serializable
data class DeviceTypeDetailModel(
    val id: DeviceTypeDetailId,
    val deviceTypeId: DeviceTypeId,
    val title: Title,
    val description: Description,
    val language: Language,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class DeviceTypeDetailCreateModel(
    val deviceTypeId: DeviceTypeId,
    val title: Title,
    val description: Description,
    val language: Language,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)
@Serializable
data class DeviceTypeDetailUpdateModel(
    val deviceTypeId: DeviceTypeId?,
    val title: Title?,
    val description: Description?,
    val language: Language?,
    val updateDate: UpdateDate?,
    val creationDate: CreationDate?
)