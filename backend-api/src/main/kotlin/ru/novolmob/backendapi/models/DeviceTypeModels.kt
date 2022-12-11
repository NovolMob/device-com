package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.ids.DeviceTypeId

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