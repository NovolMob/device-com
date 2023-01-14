package ru.novolmob.jdbcdatabase.extensions

import ru.novolmob.jdbcdatabase.tables.parameters.IParameter

object ParameterExtension {

    fun IParameter<*>.fullName() = "${databaseObject.name}.$name"

    fun List<IParameter<*>>.build() = map(IParameter<*>::buildCreationString)

}