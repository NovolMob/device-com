package ru.novolmob.jdbcdatabase.tables.columns

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Table
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue

class Constraint<T: Any>(
    override val name: String,
    private val reference: IColumn<T>,
    override val table: Table
): Column<T>(name, reference.type, table) {

    private var onDelete = ""
    private var onUpdate = ""

    fun onDeleteRestrict() = apply { onDelete = DatabaseVocabulary.ON_DELETE_RESTRICT }
    fun onUpdateRestrict() = apply { onDelete = DatabaseVocabulary.ON_UPDATE_RESTRICT }
    fun onDeleteCascade() = apply { onDelete = DatabaseVocabulary.ON_DELETE_CASCADE }
    fun onUpdateCascade() = apply { onDelete = DatabaseVocabulary.ON_UPDATE_CASCADE }

    private fun reference(): String = "${reference.table.tableName}(${reference.name})"
    private fun foreignKeyName(): String = "fk_${table.tableName}_${name}_by_${reference.table.tableName}_${reference.name}"

    override fun buildCreationString(): String =
        "${super.buildCreationString()}, ${DatabaseVocabulary.CONSTRAINT} ${foreignKeyName()} " +
                "${DatabaseVocabulary.FOREIGN_KEY} ($name) " +
                "${DatabaseVocabulary.REFERENCES} ${reference()}" +
                "${onUpdate.takeIf { it.isNotEmpty() }?.let { " $it" } ?: ""}" +
                "${onDelete.takeIf { it.isNotEmpty() }?.let { " $it" } ?: ""}"

    override infix fun valueOf(value: T): ColumnValue<T> =
        ColumnValue(columnName = name, value = value, type = type)

}