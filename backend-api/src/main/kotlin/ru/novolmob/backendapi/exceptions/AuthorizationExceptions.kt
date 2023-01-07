package ru.novolmob.backendapi.exceptions
fun badLoginModelException(): AbstractBackendException =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Login form is empty!"
    )

fun notAuthorizedException(): AbstractBackendException =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.FORBIDDEN,
        message = "You are not authorized."
    )

fun dontHaveRightsException(): AbstractBackendException =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.FORBIDDEN,
        message = "You don't have enough rights."
    )
