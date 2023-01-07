package ru.novolmob.user_mobile_app.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.Password

open class PasswordMutableValue(
    private val regex: Regex = Regex("\\w+\\d+"),
    private val notValidException: AbstractBackendException = AbstractBackendException.BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Пароли указаны неверно!"
    )
): AbstractMutableValue<Password>(Password(""), false) {
    val firstState = object: MutableValue<String> {
        protected val _value = MutableStateFlow("")
        override val value: StateFlow<String> = _value.asStateFlow()

        override val valid: StateFlow<Boolean> = _valid.asStateFlow()


        override fun set(newValue: String) {
            _value.update { newValue }
        }

        override fun get(): String = _value.value
        override fun isValid(value: String): Boolean = true
        override fun clear() {
            set("")
        }
    }
    val secondState = object: MutableValue<String> {
        protected val _value = MutableStateFlow("")
        override val value: StateFlow<String> = _value.asStateFlow()

        override val valid: StateFlow<Boolean> = _valid.asStateFlow()


        override fun set(newValue: String) {
            _value.update { newValue }
        }

        override fun get(): String = _value.value
        override fun isValid(value: String): Boolean = true
        override fun clear() {
            set("")
        }
    }

    init {
        CoroutineScope(SupervisorJob()).launch {
            combine(firstState.value, secondState.value) { first: String, second: String ->
                if (first == second) first else ""
            }.collectLatest {
                set(Password(it))
            }
        }
    }

    override fun clear() {
        firstState.clear()
        secondState.clear()
        set(Password(""))
    }

    override fun isValid(value: Password): Boolean = regex.matches(value.string)

    fun getModel(): Either<AbstractBackendException, Password> =
        get().let { value ->
            if (!isValid(value)) notValidException.left()
            else value.right()
        }
}