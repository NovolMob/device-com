package ru.novolmob.devicecom.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.*
import ru.novolmob.core.models.Firstname.Companion.firstname
import ru.novolmob.core.models.Language.Companion.language
import ru.novolmob.core.models.Lastname.Companion.lastname
import ru.novolmob.core.models.PhoneNumber.Companion.phoneNumber
import ru.novolmob.devicecom.ui.registration.RUSSIANS

abstract class AbstractCharacterMutableValue<T>(
    initial: String,
    initialValid: Boolean = false,
    private val constructor: (String) -> T,
    private val notValidException: AbstractBackendException,
): AbstractMutableValue<String>(initial, initialValid) {
    fun getModel(): Either<AbstractBackendException, T> =
        get().let { value ->
            if (!isValid(value)) notValidException.left()
            else constructor(value).right()
        }

    override fun clear() = set("")

    class FirstnameMutableValue(initial: String = ""): AbstractCharacterMutableValue<Firstname>(
        initial = initial,
        initialValid = FirstnameChecking.matches(initial),
        constructor = { it.replaceFirstChar { it.uppercase() }.firstname() },
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Имя указано неправильно! Оно должно содержать только буквы."
        )
    ) {
        override fun isValid(value: String): Boolean = FirstnameChecking.matches(value)
    }
    class LastnameMutableValue(initial: String = ""): AbstractCharacterMutableValue<Lastname>(
        initial = initial,
        initialValid = LastnameChecking.matches(initial),
        constructor = { it.replaceFirstChar { it.uppercase() }.lastname() },
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Фамилия указана неправильно! Она должно содержать только буквы."
        )
    ) {
        override fun isValid(value: String): Boolean = LastnameChecking.matches(value)
    }
    class LanguageMutableValue(initial: String = RUSSIANS.language): AbstractCharacterMutableValue<Language>(
        initial = initial,
        initialValid = Language.availableLanguages.containsValue(initial.language()),
        constructor = ::Language,
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Язык указан неправильно!"
        )
    ) {
        override fun isValid(value: String): Boolean = Language.availableLanguages.containsValue(value.language())
    }
    class PhoneNumberMutableValue(initial: String = ""): AbstractCharacterMutableValue<PhoneNumber>(
        initial = initial,
        initialValid = PhoneNumberChecking.matches(initial),
        constructor = {
            it.phoneNumber()!!
        },
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Номер телефона указан неправильно!"
        )
    ) {
        val enterRegex = Regex("\\d{0,11}")

        override fun set(newValue: String) {
            if (enterRegex.matches(newValue)) super.set(newValue)
        }

        override fun isValid(value: String): Boolean = PhoneNumberChecking.matches(value)
    }
    class EmailMutableValue(initial: String = ""): AbstractCharacterMutableValue<Email>(
        initial = initial,
        initialValid = EmailChecking.matches(initial),
        constructor = ::Email,
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Электронная почта введена неправильно!"
        )
    ) {
        override fun isValid(value: String): Boolean = EmailChecking.matches(value)
    }
    class PasswordMutableValue(initial: String = ""): AbstractCharacterMutableValue<Password>(
        initial = initial,
        initialValid = PasswordChecking.matches(initial),
        constructor = ::Password,
        notValidException = AbstractBackendException.BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Электронная почта введена неправильно!"
        )
    ) {
        override fun isValid(value: String): Boolean = PasswordChecking.matches(value)
    }

}