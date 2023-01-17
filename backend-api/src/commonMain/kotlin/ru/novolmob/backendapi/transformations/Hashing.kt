package ru.novolmob.backendapi.transformations

interface Hashing<Value>: Transformation<Value> {
    override fun invoke(value: Value): Value = hash(value)
    fun hash(originalValue: Value): Value
}