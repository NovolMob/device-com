package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.CreationDate
import ru.novolmob.database.models.UpdateDate
import ru.novolmob.database.models.ids.DeviceTypeId

@Serializable
data class DeviceTypeModel(
    val id: DeviceTypeId,
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class DeviceTypeCreateModel(
    val updateDate: UpdateDate,
    val creationDate: CreationDate
)

@Serializable
data class DeviceTypeUpdateModel(
    val updateDate: UpdateDate?,
    val creationDate: CreationDate?
)