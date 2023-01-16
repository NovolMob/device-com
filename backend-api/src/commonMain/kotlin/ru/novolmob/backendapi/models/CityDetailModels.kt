package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId

@Serializable
data class CityDetailModel(
    val id: CityDetailId,
    val parentId: CityId,
    val title: Title,
    val language: Language
)

@Serializable
data class CityDetailCreateModel(
    override val parentId: CityId,
    val title: Title,
    override val language: Language
): DetailCreate<CityId>

@Serializable
data class CityDetailUpdateModel(
    override val parentId: CityId? = null,
    val title: Title? = null,
    override val language: Language? = null
): DetailUpdate<CityId>