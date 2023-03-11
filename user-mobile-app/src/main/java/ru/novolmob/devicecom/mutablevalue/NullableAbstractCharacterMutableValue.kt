package ru.novolmob.devicecom.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.*
import ru.novolmob.core.models.Patronymic.Companion.patronymic

abstract class NullableAbstractCharacterMutableValue<T>(
    initial: String = "",
    initialValid: Boolean = true,
    private val constructor: (String) -> T,
    private val notValidException: AbstractBackendException
): AbstractMutableValue<String>(initial, initialValid) {
    fun getModel(): Either<AbstractBackendException, T?> =
        get().let { value ->
            if (!isValid(value)) notValidException.left()
            else value.let(constructor).right()
        }

    override fun clear() {
        set("")
    }

    class PatronymicMutableValue(initial: String = ""): NullableAbstractCharacterMutableValue<Patronymic>(
        initial = initial,
        initialValid = initial.isEmpty() || PatronymicChecking.matches(initial),
        constructor = { it.replaceFirstChar { it.uppercase() }.patronymic() },
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Отчество введено неправильно! Оно должно содержать только буквы."
        )
    ) {
        override fun isValid(value: String): Boolean = value.isEmpty() || value.let(PatronymicChecking::matches)
    }
    class EmailMutableValue(initial: String = ""): NullableAbstractCharacterMutableValue<Email>(
        initial = initial,
        initialValid = initial.isEmpty() || EmailChecking.matches(initial),
        constructor = ::Email,
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Электронная почта введена неправильно!"
        )
    ) {
        override fun isValid(value: String): Boolean = value.isEmpty() || value.let(EmailChecking::matches)
    }

}