package ru.novolmob.jdbcdatabase.tables.parameters

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

open class Constraint<T: Any>(
    override val name: String,
    private val reference: IParameter<T>,
    override val databaseObject: DatabaseObject
): Column<T>(name, reference.type, databaseObject) {

    var onDelete = ""
        protected set
    var onUpdate = ""
        protected set

    fun onDeleteRestrict() = apply { onDelete = DatabaseVocabulary.ON_DELETE_RESTRICT }
    fun onUpdateRestrict() = apply { onDelete = DatabaseVocabulary.ON_UPDATE_RESTRICT }
    fun onDeleteCascade() = apply { onDelete = DatabaseVocabulary.ON_DELETE_CASCADE }
    fun onUpdateCascade() = apply { onDelete = DatabaseVocabulary.ON_UPDATE_CASCADE }

    override fun nullable() = apply { nullable = true }
    override fun unique() = apply { unique = true }
    override fun primaryKey() = apply { primaryKey = true }
    override fun default(sql: String) = apply { default = sql }

    private fun reference(): String = "${reference.databaseObject.name}(${reference.name})"
    private fun foreignKeyName(): String = "fk_${databaseObject.name}_${name}_by_${reference.databaseObject.name}_${reference.name}"

    override fun buildCreationString(): String =
        "${super.buildCreationString()}, " +
                DatabaseVocabulary.foreignKey(
                    foreignKeyName(), name, reference(),
                    listOf(onDelete, onUpdate).filter { it.isNotEmpty() }
                )

    override infix fun valueOf(value: T?): ParameterValue<T> =
        ParameterValue(parameterName = name, value = value, type = type)

}