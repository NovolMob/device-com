package ru.novolmob.backendapi.exceptions

import ru.novolmob.core.models.ids.UserId

fun userByIdNotFound(userId: UserId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "User with id $userId not found!"
    )

fun failedToCreateUser() =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.UNKNOWN,
        message = "Failed to create user!"
    )