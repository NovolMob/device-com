package ru.novolmob.jdbcdatabase.tables

import org.koin.core.component.KoinComponent
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.set
import ru.novolmob.jdbcdatabase.tables.columns.Column
import ru.novolmob.jdbcdatabase.tables.columns.Constraint
import ru.novolmob.jdbcdatabase.tables.columns.IColumn
import ru.novolmob.jdbcdatabase.tables.columns.types.IColumnType
import ru.novolmob.jdbcdatabase.tables.columns.types.PrimitiveColumnType
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression
import ru.novolmob.jdbcdatabase.utils.DatabaseObjectUtil.tableName

abstract class Table(name: String? = null): KoinComponent, DatabaseObject() {
    val tableName = name ?: javaClass.tableName()
    private val _columns = mutableListOf<IColumn<*>>()
    val columns: List<IColumn<*>>
        get() = _columns.toList()

    override fun create() {
        val sql = DatabaseVocabulary.createTableSql(tableName, columns.map(IColumn<*>::buildCreationString)).also { println("Table $tableName created:  $it") }
        database.statement(sql)
    }
    fun <T: Any> registerColumn(name: String, type: IColumnType<T>): Column<T> =
        Column(name, type, this).also { _columns.add(it) }

    fun <T: Any> reference(name: String, reference: IColumn<T>): Constraint<T> =
        Constraint(name, reference, this).also { _columns.add(it) }

    fun text(name: String) = registerColumn(name, PrimitiveColumnType.Text)
    fun decimal(name: String, precision: Int, scale: Int) =
        registerColumn(name, PrimitiveColumnType.Decimal(precision, scale))
    fun varchar(name: String, n: Int) = registerColumn(name, PrimitiveColumnType.VarChar(n))
    fun uuid(name: String) = registerColumn(name, PrimitiveColumnType.Uuid)
    fun integer(name: String) = registerColumn(name, PrimitiveColumnType.Integer)
    fun date(name: String) = registerColumn(name, PrimitiveColumnType.Date)

    fun update(vararg newValues: ColumnValue<*>, expression: Expression? = null): Int {
        val sql = DatabaseVocabulary.updateSql(tableName = tableName, newValues.map { it.columnName }, expression?.sqlString).also { println("Update table $tableName:  $it") }
        return database.updateStatement(sql) {
            newValues.forEachIndexed { index, value ->
                set(index + 1, value)
            }
            expression?.valueOrder?.forEachIndexed { index, value ->
                set(index + newValues.size + 1, value)
            }
        }
    }

    fun insert(vararg values: ColumnValue<*>): Int {
        val sql = DatabaseVocabulary.insertSql(tableName = tableName, values.map { it.columnName }).also { println("Insert into table $tableName:  $it") }
        return database.updateStatement(sql) {
            values.forEachIndexed { index, value ->
                set(index + 1, value)
            }
        }
    }

}