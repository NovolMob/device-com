package utils

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import navigations.Navigation
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode

object EitherUtil {
    fun <T, R> Either<AbstractBackendException, T>.loginChecking(block: (T) -> R): Either<AbstractBackendException, R> = fold(
        ifLeft = {
            if (it.code == BackendExceptionCode.NOT_AUTHORIZED) {
                Navigation.toLogin()
            }
            it.left()
        },
        ifRight = {
            block(it).right()
        }
    )
}