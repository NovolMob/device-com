package ru.novolmob.backendapi.exceptions
fun badLoginModelException(): BackendException =
    BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Login form is empty!"
    )