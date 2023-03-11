package ru.novolmob.backendapi.transformations

interface Encryption<Value>: Transformation<Value> {
    override fun invoke(value: Value): Value = encrypt(value)
    fun encrypt(originalValue: Value): Value
    fun decrypt(encryptedValue: Value): Value
}