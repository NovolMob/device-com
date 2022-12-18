package ru.novolmob.backend.exceptions

import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.ids.UserId

fun userByIdNotFound(userId: UserId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "User with id $userId not found!"
    )