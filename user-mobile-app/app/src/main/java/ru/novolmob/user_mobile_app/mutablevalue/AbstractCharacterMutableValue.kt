package ru.novolmob.user_mobile_app.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.*
import ru.novolmob.core.utils.PhoneNumberUtil
import ru.novolmob.user_mobile_app.ui.registration.RUSSIANS
import ru.novolmob.user_mobile_app.utils.PhoneNumberOffsetMapping
import java.util.*

abstract class AbstractCharacterMutableValue<T>(
    initial: String,
    initialValid: Boolean = false,
    private val constructor: (String) -> T,
    private val notValidException: BackendException,
): AbstractMutableValue<String>(initial, initialValid) {
    fun getModel(): Either<BackendException, T> =
        get().let { value ->
            if (!isValid(value)) notValidException.left()
            else constructor(value).right()
        }

    override fun clear() = set("")

    class FirstnameMutableValue(initial: String = ""): AbstractCharacterMutableValue<Firstname>(
        initial = initial,
        initialValid = regex.matches(initial),
        constructor = { Firstname(it.replaceFirstChar { it.uppercase() }) },
        notValidException = BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Имя указано неправильно! Оно должно содержать только буквы."
        )
    ) {
        companion object {
            val regex = Regex("[a-zA-Zа-яА-я]+")
        }

        override fun isValid(value: String): Boolean = regex.matches(value)
    }
    class LastnameMutableValue(initial: String = ""): AbstractCharacterMutableValue<Lastname>(
        initial = initial,
        initialValid = regex.matches(initial),
        constructor = { Lastname(it.replaceFirstChar { it.uppercase() }) },
        notValidException = BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Фамилия указана неправильно! Она должно содержать только буквы."
        )
    ) {
        companion object {
            val regex = Regex("[a-zA-Zа-яА-я]+")
        }

        override fun isValid(value: String): Boolean = regex.matches(value)
    }
    class LanguageMutableValue(initial: String = RUSSIANS.language): AbstractCharacterMutableValue<Language>(
        initial = initial,
        initialValid = availableLanguages.any { it.language == initial },
        constructor = ::Language,
        notValidException = BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Язык указан неправильно!"
        )
    ) {
        companion object {
            val availableLanguages: List<Locale> = listOf(
                RUSSIANS
            )
        }

        override fun isValid(value: String): Boolean = availableLanguages.any { it.language == value }
    }
    class PhoneNumberMutableValue(initial: String = ""): AbstractCharacterMutableValue<PhoneNumber>(
        initial = initial,
        initialValid = regex.matches(initial),
        constructor = {
            PhoneNumberUtil.deserializePhoneNumber(PhoneNumberOffsetMapping.phoneNumber(it))!!
        },
        notValidException = BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Номер телефона указан неправильно!"
        )
    ) {
        companion object {
            val regex = Regex("\\d{11}")
        }
        val enterRegex = Regex("\\d{0,11}")

        override fun set(newValue: String) {
            if (enterRegex.matches(newValue)) super.set(newValue)
        }

        override fun isValid(value: String): Boolean = regex.matches(value)
    }
    class EmailMutableValue(initial: String = ""): AbstractCharacterMutableValue<Email>(
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

        override fun isValid(value: String): Boolean = regex.matches(value)
    }
    class PasswordMutableValue(initial: String = ""): AbstractCharacterMutableValue<Password>(
        initial = initial,
        initialValid = regex.matches(initial),
        constructor = ::Password,
        notValidException = BackendException(
            code = BackendExceptionCode.BAD_REQUEST,
            message = "Электронная почта введена неправильно!"
        )
    ) {
        companion object {
            val regex = Regex("\\w+\\d+")
        }

        override fun isValid(value: String): Boolean = regex.matches(value)
    }

}