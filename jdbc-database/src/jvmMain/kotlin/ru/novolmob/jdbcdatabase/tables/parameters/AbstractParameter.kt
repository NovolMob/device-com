package ru.novolmob.jdbcdatabase.tables.parameters

import ru.novolmob.jdbcdatabase.extensions.ParameterExtension.fullName

abstract class AbstractParameter<T: Any>: IParameter<T> {
    override fun toString(): String = fullName()
}