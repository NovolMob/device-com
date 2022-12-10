package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.CreationDate
import ru.novolmob.database.models.Title
import ru.novolmob.database.models.ids.GrantedRightId
import ru.novolmob.database.models.ids.WorkerId

@Serializable
data class GrantedRightModel(
    val id: GrantedRightId,
    val workerId: WorkerId,
    val title: Title,
    val admin: WorkerId,
    val creationDate: CreationDate
)

@Serializable
data class GrantedRightCreateModel(
    val workerId: WorkerId,
    val title: Title,
    val admin: WorkerId,
    val creationDate: CreationDate
)

@Serializable
data class GrantedRightUpdateModel(
    val workerId: WorkerId?,
    val title: Title?,
    val admin: WorkerId?,
    val creationDate: CreationDate?
)