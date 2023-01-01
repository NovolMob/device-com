package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.core.models.ids.WorkerId

@Serializable
data class WorkerCredentialModel(
    val id: CredentialId,
    val workerId: WorkerId,
    val phoneNumber: PhoneNumber,
    val email: Email? = null,
    val password: Password
)

@Serializable
data class WorkerCredentialCreateModel(
    val workerId: WorkerId,
    val phoneNumber: PhoneNumber,
    val email: Email? = null,
    val password: Password
)

@Serializable
data class WorkerCredentialUpdateModel(
    val workerId: WorkerId? = null,
    val phoneNumber: PhoneNumber? = null,
    val email: Email? = null,
    val password: Password? = null
)