package ru.novolmob.user_mobile_app.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.Email
import ru.novolmob.core.models.Patronymic
import ru.novolmob.core.models.ids.CityId
import java.util.*

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
        initialValid = regex.matches(initial),
        constructor = { Patronymic(it.replaceFirstChar { it.uppercase() }) },
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Отчество введено неправильно! Оно должно содержать только буквы."
        )
    ) {
        companion object {
            val regex = Regex("[a-zA-Zа-яА-я]+")
        }

        override fun isValid(value: String): Boolean = value.isEmpty() || value.let(regex::matches)
    }
    class CityMutableValue(initial: String = ""): NullableAbstractCharacterMutableValue<CityId>(
        initial = initial,
        initialValid = initial.isEmpty(),
        constructor = { CityId(UUID.fromString(it)) },
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Город введён неправильно"
        )
    ) {
        override fun isValid(value: String): Boolean = value.isEmpty()
    }
    class EmailMutableValue(initial: String = ""): NullableAbstractCharacterMutableValue<Email>(
        initial = initial,
        initialValid = regex.matches(initial),
        constructor = ::Email,
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Электронная почта введена неправильно!"
        )
    ) {
        companion object {
            val regex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        }

        override fun isValid(value: String): Boolean = value.isEmpty() || value.let(regex::matches)
    }

}