package ru.novolmob.backendapi.exceptions

import ru.novolmob.core.models.ids.GrantedRightId

fun grantedRightByIdNotFound(grantedRightId: GrantedRightId) =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_FOUND,
        message = "GrantedRight with id $grantedRightId not found!"
    )