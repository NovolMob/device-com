package ru.novolmob.backendapi.exceptions
fun badLoginModelException(): BackendException =
    BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Login form is empty!"
    )

fun notAuthorizedException(): BackendException =
    BackendException(
        code = BackendExceptionCode.FORBIDDEN,
        message = "You are not authorized."
    )

fun dontHaveRightsException(): BackendException =
    BackendException(
        code = BackendExceptionCode.FORBIDDEN,
        message = "You don't have enough rights."
    )
