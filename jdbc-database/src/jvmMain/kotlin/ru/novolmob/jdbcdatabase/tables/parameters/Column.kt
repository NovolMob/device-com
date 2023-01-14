package ru.novolmob.jdbcdatabase.tables.parameters

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

open class Column<T: Any>(
    override val name: String,
    override val type: IParameterType<T>,
    override val databaseObject: DatabaseObject
): AbstractParameter<T>() {
    var nullable = false
        protected set
    var unique = false
        protected set
    var primaryKey = false
        protected set
    var default: String = ""
        protected set

    open fun nullable() = apply { nullable = true }
    open fun unique() = apply { unique = true }
    open fun primaryKey() = apply { primaryKey = true }
    open fun default(sql: String) = apply { default = sql }

    override fun buildCreationString(): String {
        val list = mutableListOf(name, type.databaseType)

        if (unique) list.add(DatabaseVocabulary.UNIQUE)

        if (nullable) list.add(DatabaseVocabulary.NULL)
        else list.add(DatabaseVocabulary.NOT_NULL)

        if (default.isNotEmpty()) list.add(DatabaseVocabulary.default(default))

        return list.joinToString(" ")
    }

    override infix fun valueOf(value: T?): ParameterValue<T> =
        ParameterValue(parameterName = name, value = value, type = type)

}