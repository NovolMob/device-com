package ru.novolmob.user_mobile_app.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.*

abstract class NullableAbstractCharacterMutableValue<T>(
    initial: String = "",
    initialValid: Boolean = true,
    private val constructor: (String) -> T,
    private val notValidException: BackendException
): AbstractMutableValue<String>(initial, initialValid) {
    fun getModel(): Either<BackendException, T?> =
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
        notValidException = BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Отчество введено неправильно! Оно должно содержать только буквы."
        )
    ) {
        companion object {
            val regex = Regex("[a-zA-Zа-яА-я]+")
        }

        override fun isValid(value: String): Boolean = value.isEmpty() || value.let(regex::matches)
    }
    class CityMutableValue(initial: String = ""): NullableAbstractCharacterMutableValue<City>(
        initial = initial,
        initialValid = initial.isEmpty() || availableCities.contains(initial),
        constructor = ::City,
        notValidException = BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Город введён неправильно"
        )
    ) {
        companion object {
            val availableCities: List<String> = listOf(
                "Великий Новгород"
            )
        }

        override fun isValid(value: String): Boolean = value.isEmpty() || availableCities.contains(value)
    }
    class EmailMutableValue(initial: String = ""): NullableAbstractCharacterMutableValue<Email>(
        initial = initial,
        initialValid = regex.matches(initial),
        constructor = ::Email,
        notValidException = BackendException(
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