package ru.novolmob.devicecom.mutablevalue

import kotlinx.coroutines.flow.StateFlow

interface MutableValue<T> {
    val value: StateFlow<T>
    val valid: StateFlow<Boolean>
    fun set(newValue: T)
    fun isValid(value: T): Boolean
    fun get(): T
    fun clear()
}