package ru.novolmob.jdbcdatabase.tables.parameters

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

open class FunctionParameter<T: Any>(
    override val name: String,
    override val type: IParameterType<T>,
    override val databaseObject: DatabaseObject
): AbstractParameter<T>() {
    private var default: String = ""

    fun default(sql: String) = apply { default = sql }
    override fun buildCreationString(): String {
        val list = mutableListOf(name, type.databaseType)

        if (default.isNotEmpty()) list.add(DatabaseVocabulary.default(default))

        return list.joinToString(" ")
    }

    override infix fun valueOf(value: T?): ParameterValue<T> = ParameterValue(name, value, type)

}