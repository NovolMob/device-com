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
    val parentId: DeviceTypeId,
    val title: Title,
    val description: Description,
    val language: Language
)

@Serializable
data class DeviceTypeDetailCreateModel(
    override val parentId: DeviceTypeId,
    val title: Title,
    val description: Description,
    override val language: Language
): DetailCreate<DeviceTypeId>

@Serializable
data class DeviceTypeDetailUpdateModel(
    override val parentId: DeviceTypeId? = null,
    val title: Title? = null,
    val description: Description? = null,
    override val language: Language? = null
): DetailUpdate<DeviceTypeId>