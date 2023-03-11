package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Code
import ru.novolmob.core.models.ids.GrantedRightId
import ru.novolmob.core.models.ids.WorkerId

@Serializable
data class GrantedRightModel(
    val id: GrantedRightId,
    val workerId: WorkerId,
    val code: Code,
    val adminId: WorkerId
)

@Serializable
data class GrantedRightCreateModel(
    val workerId: WorkerId,
    val code: Code,
    val adminId: WorkerId
)

@Serializable
data class GrantedRightUpdateModel(
    val workerId: WorkerId? = null,
    val code: Code? = null,
    val adminId: WorkerId? = null
)