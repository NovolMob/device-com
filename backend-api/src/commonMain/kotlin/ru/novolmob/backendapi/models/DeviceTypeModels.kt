package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.DeviceTypeId

@Serializable
data class DeviceTypeModel(
    val id: DeviceTypeId
)

@Serializable
class DeviceTypeCreateModel()

@Serializable
class DeviceTypeUpdateModel()

@Serializable
data class DeviceTypeFullModel(
    val id: DeviceTypeId,
    val detail: DeviceTypeDetailModel
)

@Serializable
data class DeviceTypeShortModel(
    val id: DeviceTypeId,
    val title: Title
)