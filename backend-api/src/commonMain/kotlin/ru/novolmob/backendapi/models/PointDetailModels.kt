package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Address
import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Schedule
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId

@Serializable
data class PointDetailModel(
    val id: PointDetailId,
    val parentId: PointId,
    val address: Address,
    val schedule: Schedule,
    val description: Description,
    val language: Language
)

@Serializable
data class PointDetailCreateModel(
    override val parentId: PointId,
    val address: Address,
    val schedule: Schedule,
    val description: Description,
    override val language: Language
): DetailCreate<PointId>

@Serializable
data class PointDetailUpdateModel(
    override val parentId: PointId? = null,
    val address: Address? = null,
    val schedule: Schedule? = null,
    val description: Description? = null,
    override val language: Language? = null
): DetailUpdate<PointId>