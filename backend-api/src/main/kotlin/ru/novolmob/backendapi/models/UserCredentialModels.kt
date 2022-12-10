package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.database.models.Email
import ru.novolmob.database.models.Password
import ru.novolmob.database.models.PhoneNumber
import ru.novolmob.database.models.UpdateDate
import ru.novolmob.database.models.ids.CredentialId
import ru.novolmob.database.models.ids.UserId

@Serializable
data class UserCredentialModel(
    val id: CredentialId,
    val userId: UserId,
    val phoneNumber: PhoneNumber,
    val email: Email?,
    val password: Password,
    val updateDate: UpdateDate
)

@Serializable
data class UserCredentialCreateModel(
    val userId: UserId,
    val phoneNumber: PhoneNumber,
    val email: Email?,
    val password: Password,
    val updateDate: UpdateDate
)

@Serializable
data class UserCredentialUpdateModel(
    val userId: UserId?,
    val phoneNumber: PhoneNumber?,
    val email: Email?,
    val password: Password?,
    val updateDate: UpdateDate?
)