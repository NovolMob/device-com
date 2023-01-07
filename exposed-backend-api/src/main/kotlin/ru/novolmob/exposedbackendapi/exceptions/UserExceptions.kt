package ru.novolmob.exposedbackendapi.exceptions

import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.ids.UserId

fun userByIdNotFound(userId: UserId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "User with id $userId not found!"
    )