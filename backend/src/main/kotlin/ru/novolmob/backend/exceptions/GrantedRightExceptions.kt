package ru.novolmob.backend.exceptions

import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.database.models.ids.GrantedRightId

fun grantedRightByIdNotFound(grantedRightId: GrantedRightId) =
    BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "GrantedRight with id $grantedRightId not found!"
    )