package utils

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException

object EitherUtil {
    fun <T, R> Either<AbstractBackendException, T>.ifRight(block: (T) -> R): Either<AbstractBackendException, R> = fold(
        ifLeft = {
            it.left()
        },
        ifRight = {
            block(it).right()
        }
    )
}