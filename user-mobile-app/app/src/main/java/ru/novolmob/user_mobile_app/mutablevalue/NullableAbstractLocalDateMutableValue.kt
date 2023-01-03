package ru.novolmob.user_mobile_app.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.datetime.LocalDate
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.extensions.LocalDateTimeExtension.now
import ru.novolmob.core.models.Birthday

abstract class NullableAbstractLocalDateMutableValue<T>(
    initial: LocalDate?,
    initialValid: Boolean = true,
    val constructor: (LocalDate) -> T,
    private val notValidException: BackendException
): AbstractMutableValue<LocalDate?>(initial, initialValid) {
    fun getModel(): Either<BackendException, T?> =
        get().let { value ->
            if (!isValid(value)) notValidException.left()
            else value?.let(constructor).right()
        }

    class BirthdayMutableValue(initial: LocalDate? = null): NullableAbstractLocalDateMutableValue<Birthday>(
        initial = initial,
        initialValid = initial?.let { it < LocalDate.now() } ?: true,
        constructor = ::Birthday,
        notValidException = BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Дата рождения указана нерпавильно!"
        )
    ) {
        override fun isValid(value: LocalDate?): Boolean = value?.let { it < LocalDate.now() } ?: true
    }

}
