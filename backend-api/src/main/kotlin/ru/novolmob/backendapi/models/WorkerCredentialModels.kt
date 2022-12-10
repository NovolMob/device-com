package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.Email
import ru.novolmob.database.models.Password
import ru.novolmob.database.models.PhoneNumber
import ru.novolmob.database.models.UpdateDate
import ru.novolmob.database.models.ids.CredentialId
import ru.novolmob.database.models.ids.WorkerId

@Serializable
data class WorkerCredentialModel(
    val id: CredentialId,
    val workerId: WorkerId,
    val phoneNumber: PhoneNumber,
    val email: Email?,
    val password: Password,
    val updateDate: UpdateDate
)

@Serializable
data class WorkerCredentialCreateModel(
    val workerId: WorkerId,
    val phoneNumber: PhoneNumber,
    val email: Email?,
    val password: Password,
    val updateDate: UpdateDate
)

@Serializable
data class WorkerCredentialUpdateModel(
    val workerId: WorkerId?,
    val phoneNumber: PhoneNumber?,
    val email: Email?,
    val password: Password?,
    val updateDate: UpdateDate?
)