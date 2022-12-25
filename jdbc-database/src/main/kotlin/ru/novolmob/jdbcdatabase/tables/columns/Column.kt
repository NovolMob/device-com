package ru.novolmob.jdbcdatabase.tables.columns

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Table
import ru.novolmob.jdbcdatabase.tables.columns.types.IColumnType
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue

open class Column<T: Any>(
    override val name: String,
    override val type: IColumnType<T>,
    override val table: Table
): IColumn<T> {
    private var nullable = false
    var unique = false
    private var primaryKey = false
    private var default: String = ""

    fun nullable() = apply { nullable = true }
    fun unique() = apply { unique = true }
    fun primaryKey() = apply { primaryKey = true }
    fun default(sql: String) = apply { default = sql }

    override fun buildCreationString(): String {
        val list = mutableListOf(name, type.databaseType)

        if (unique) list.add(DatabaseVocabulary.UNIQUE)

        if (nullable) list.add(DatabaseVocabulary.NULL)
        else list.add(DatabaseVocabulary.NOT_NULL)

        if (primaryKey) list.add(DatabaseVocabulary.PRIMARY_KEY)

        if (default.isNotEmpty()) list.add(DatabaseVocabulary.default(default))

        return list.joinToString(" ")
    }

    override infix fun valueOf(value: T): ColumnValue<T> =
        ColumnValue(columnName = name, value = value, type = type)

}