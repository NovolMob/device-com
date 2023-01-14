package ru.novolmob.backendapi.utils

import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode

object ThrowableUtil {

    fun Throwable.backendException(): AbstractBackendException =
        AbstractBackendException.BackendException(
            code = BackendExceptionCode.UNKNOWN,
            message = message ?: "Unknown error"
        )

}