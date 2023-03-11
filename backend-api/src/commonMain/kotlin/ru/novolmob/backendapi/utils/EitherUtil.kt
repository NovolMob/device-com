package ru.novolmob.backendapi.utils

import arrow.core.Either
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.utils.ThrowableUtil.backendException

object EitherUtil {

    inline fun <R> Either.Companion.backend(right: () -> R): Either<AbstractBackendException, R> =
        Either.Companion.catch(
            { it.printStackTrace(); it.backendException() },
            right
        )

}