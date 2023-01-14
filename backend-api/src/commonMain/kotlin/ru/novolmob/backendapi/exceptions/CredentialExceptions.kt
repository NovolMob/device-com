package ru.novolmob.backendapi.exceptions

import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.core.models.ids.WorkerId

fun credentialByIdNotFoundException(credentialId: CredentialId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "Credential row with id $credentialId not found!"
    )

fun userCredentialByUserIdNotFoundException(userId: UserId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "Credential for user $userId not found!"
    )

fun workerCredentialByWorkerIdNotFoundException(workerId: WorkerId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        "Credential for worker $workerId not found!"
    )

fun badCredentialsException() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Credentials is wrong"
    )

fun emailOrPhoneNumberNotUnique() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Email or phone number is exists!"
    )