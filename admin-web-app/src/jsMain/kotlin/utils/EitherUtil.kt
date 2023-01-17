package utils

import arrow.core.Either
import naavigations.Navigation
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode

object EitherUtil {
    fun <T> Either<AbstractBackendException, T>.loginChecking(block: (T) -> Unit) = fold(
        ifLeft = {
            if (it.code == BackendExceptionCode.NOT_AUTHORIZED) {
                Navigation.toLogin()
            }
        },
        ifRight = block
    )
}