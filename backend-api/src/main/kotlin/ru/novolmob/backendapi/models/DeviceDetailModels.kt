package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Features
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId

@Serializable
data class DeviceDetailModel(
    val id: DeviceDetailId,
    val deviceId: DeviceId,
    val title: Title,
    val description: Description,
    val features: Features,
    val language: Language
)
@Serializable
data class DeviceDetailCreateModel(
    val deviceId: DeviceId,
    val title: Title,
    val description: Description,
    val features: Features,
    val language: Language
)

@Serializable
data class DeviceDetailUpdateModel(
    val deviceId: DeviceId? = null,
    val title: Title? = null,
    val description: Description? = null,
    val features: Features? = null,
    val language: Language? = null
)