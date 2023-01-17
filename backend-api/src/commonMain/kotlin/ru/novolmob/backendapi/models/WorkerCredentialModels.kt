package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Password
import ru.novolmob.core.models.PhoneNumber
import ru.novolmob.core.models.ids.WorkerId

interface WorkerCredentialUpdate {
    val phoneNumber: PhoneNumber?
    val email: Email?
    val password: Password?
}

@Serializable
data class WorkerCredentialModel(
    val workerId: WorkerId,
    val phoneNumber: PhoneNumber,
    val email: Email? = null,
    val password: Password
)