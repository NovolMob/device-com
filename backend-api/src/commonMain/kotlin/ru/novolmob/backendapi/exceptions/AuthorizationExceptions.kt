package ru.novolmob.backendapi.exceptions
fun badLoginModelException(): AbstractBackendException =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Login form is empty!"
    )

fun notAuthorizedException(): AbstractBackendException =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.NOT_AUTHORIZED,
        message = "You are not authorized."
    )

fun dontHaveRightsException(): AbstractBackendException =
    AbstractBackendException.BackendException(
        code = BackendExceptionCode.DONT_HAVE_RIGHT,
        message = "You don't have enough rights."
    )
