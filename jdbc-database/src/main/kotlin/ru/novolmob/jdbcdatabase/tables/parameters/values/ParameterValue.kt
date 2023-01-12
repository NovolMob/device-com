package ru.novolmob.jdbcdatabase.tables.parameters.values

import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType

open class ParameterValue<T>(
    val parameterName: String,
    private val value: T?,
    val type: IParameterType<T>
) {
    val dbValue: Any?
        get() = value?.let { type.toDbType(value) }
}