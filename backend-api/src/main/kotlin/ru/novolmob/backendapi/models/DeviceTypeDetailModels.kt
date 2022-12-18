package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId

@Serializable
data class DeviceTypeDetailModel(
    val id: DeviceTypeDetailId,
    val deviceTypeId: DeviceTypeId,
    val title: Title,
    val description: Description,
    val language: Language
)

@Serializable
data class DeviceTypeDetailCreateModel(
    val deviceTypeId: DeviceTypeId,
    val title: Title,
    val description: Description,
    val language: Language
)
@Serializable
data class DeviceTypeDetailUpdateModel(
    val deviceTypeId: DeviceTypeId?,
    val title: Title?,
    val description: Description?,
    val language: Language?
)