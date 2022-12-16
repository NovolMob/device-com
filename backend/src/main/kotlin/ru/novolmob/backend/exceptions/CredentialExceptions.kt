package ru.novolmob.backend.exceptions

import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.database.models.ids.CredentialId
import ru.novolmob.database.models.ids.UserId
import ru.novolmob.database.models.ids.WorkerId

fun credentialByIdNotFoundException(credentialId: CredentialId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "Credential row with id $credentialId not found!"
    )

fun userCredentialByUserIdNotFoundException(userId: UserId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "Credential for user $userId not found!"
    )

fun workerCredentialByUserIdNotFoundException(workerId: WorkerId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "Credential for worker $workerId not found!"
    )

fun badCredentialsException() =
    BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Credentials is wrong"
    )