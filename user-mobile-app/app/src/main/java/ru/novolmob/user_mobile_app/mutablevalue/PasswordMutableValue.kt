package ru.novolmob.user_mobile_app.mutablevalue

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.novolmob.backendapi.exceptions.BackendException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode
import ru.novolmob.core.models.Password

open class PasswordMutableValue(
    private val regex: Regex = Regex("\\w+\\d+"),
    private val notValidException: BackendException = BackendException(
        code = BackendExceptionCode.BAD_REQUEST,
        message = "Пароли указаны неверно!"
    )
): AbstractMutableValue<String>("", false) {
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
            }.collectLatest(::set)
        }
    }

    override fun clear() {
        firstState.clear()
        secondState.clear()
        set("")
    }

    override fun isValid(value: String): Boolean = regex.matches(value)

    fun getModel(): Either<BackendException, Password> =
        get().let { value ->
            if (!isValid(value)) notValidException.left()
            else Password(value).right()
        }
}