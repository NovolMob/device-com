package ru.novolmob.devicecom.mutablevalue

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class AbstractMutableValue<T>(initial: T, initialValid: Boolean): MutableValue<T> {
    protected val _value = MutableStateFlow(initial)
    override val value: StateFlow<T> = _value.asStateFlow()

    protected val _valid = MutableStateFlow(initialValid)
    override val valid: StateFlow<Boolean> = _valid.asStateFlow()


    override fun set(newValue: T) {
        _value.update { newValue }
        _valid.update { isValid(newValue) }
    }

    override fun get(): T = _value.value

}