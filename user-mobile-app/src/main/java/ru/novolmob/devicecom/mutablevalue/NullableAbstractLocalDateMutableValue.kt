package ru.novolmob.devicecom.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.datetime.LocalDate
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.extensions.LocalDateTimeExtension.now
import ru.novolmob.core.models.Birthday

abstract class NullableAbstractLocalDateMutableValue<T>(
    initial: LocalDate?,
    initialValid: Boolean = true,
    val constructor: (LocalDate) -> T,
    private val notValidException: AbstractBackendException
): AbstractMutableValue<LocalDate?>(initial, initialValid) {
    fun getModel(): Either<AbstractBackendException, T?> =
        get().let { value ->
            if (!isValid(value)) notValidException.left()
            else value?.let(constructor).right()
        }

    override fun clear() {
        set(null)
    }

    class BirthdayMutableValue(initial: LocalDate? = null): NullableAbstractLocalDateMutableValue<Birthday>(
        initial = initial,
        initialValid = initial?.let { it < LocalDate.now() } ?: true,
        constructor = ::Birthday,
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Дата рождения указана нерпавильно!"
        )
    ) {
        override fun isValid(value: LocalDate?): Boolean = value?.let { it < LocalDate.now() } ?: true
    }

}
