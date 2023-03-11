package ru.novolmob.jdbcdatabase.tables.parameters

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.ParameterExtension.fullName
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

class ViewParameter<T: Any>(
    private val newName: String? = null,
    val reference: IParameter<T>,
    override val databaseObject: DatabaseObject
) : AbstractParameter<T>() {
    override val type: IParameterType<T> = reference.type
    override val name: String = newName ?: reference.name

    override fun buildCreationString(): String = if (newName != null) DatabaseVocabulary.asNewName(reference.fullName(), newName) else reference.fullName()

    override infix fun valueOf(value: T?): ParameterValue<T> =
        ParameterValue(name, value, type)
}