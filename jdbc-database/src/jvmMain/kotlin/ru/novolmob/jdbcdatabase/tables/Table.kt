package ru.novolmob.jdbcdatabase.tables

import org.koin.core.component.KoinComponent
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.extensions.ParameterExtension.build
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.set
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.use
import ru.novolmob.jdbcdatabase.tables.parameters.Column
import ru.novolmob.jdbcdatabase.tables.parameters.Constraint
import ru.novolmob.jdbcdatabase.tables.parameters.IParameter
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType
import ru.novolmob.jdbcdatabase.tables.parameters.types.PrimitiveParameterType
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression
import ru.novolmob.jdbcdatabase.utils.DatabaseObjectUtil.tableName
import java.sql.ResultSet

abstract class Table(name: String? = null): KoinComponent, DatabaseObject() {
    override val name = name ?: javaClass.tableName()
    private val _columns = mutableListOf<Column<*>>()
    val columns: List<Column<*>>
        get() = _columns.toList()

    override suspend fun create() {
        val sql = DatabaseVocabulary.createTableSql(
            name, columns.build(),
            columns.filter { it.primaryKey }.map { it.name }
        ).also { println("Table $name created:  $it") }
        database.statement(sql)
    }
    fun <T: Any> registerColumn(name: String, type: IParameterType<T>): Column<T> =
        Column(name, type, this).also { _columns.add(it) }

    fun <T: Any> reference(name: String, reference: IParameter<T>): Constraint<T> =
        Constraint(name, reference, this).also { _columns.add(it) }

    fun text(name: String) = registerColumn(name, PrimitiveParameterType.Text)
    fun decimal(name: String, precision: Int, scale: Int) =
        registerColumn(name, PrimitiveParameterType.Decimal(precision, scale))
    fun varchar(name: String, n: Int) = registerColumn(name, PrimitiveParameterType.VarChar(n))
    fun uuid(name: String) = registerColumn(name, PrimitiveParameterType.Uuid)
    fun integer(name: String) = registerColumn(name, PrimitiveParameterType.Integer)
    fun date(name: String) = registerColumn(name, PrimitiveParameterType.Date)
    fun time(name: String) = registerColumn(name, PrimitiveParameterType.Timestamp)
    fun bool(name: String) = registerColumn(name, PrimitiveParameterType.Boolean)

    suspend fun update(vararg newValues: ParameterValue<*>, expression: Expression? = null): Int {
        val sql = DatabaseVocabulary.updateSql(tableName = name, newValues.map { it.parameterName }, expression?.sqlString).also { println("Update table $name:  $it") }
        return database.updateStatement(sql) {
            newValues.forEachIndexed { index, value ->
                set(index + 1, value)
            }
            expression?.valueOrder?.forEachIndexed { index, value ->
                set(index + newValues.size + 1, value)
            }
        }
    }

    suspend fun insert(vararg values: ParameterValue<*>): Int {
        val sql = DatabaseVocabulary.insertSql(tableName = name, values.map { it.parameterName }).also { println("Insert into table $name:  $it") }
        return database.updateStatement(sql) {
            values.forEachIndexed { index, value ->
                set(index + 1, value)
            }
        }
    }

    suspend fun <T> select(
        page: Int?, pageSize: Int?,
        sortByColumn: String? = null,
        sortOrder: String? = null,
        block: suspend ResultSet.() -> T
    ): T =
        select(
            limit = pageSize, offset = pageSize?.let { it * (page ?: 0) },
            orderBy = sortByColumn?.let { listOf(it + (sortOrder?.let { " $it" } ?: "")) },
            block = block
        )

    suspend fun <T> select(
        vararg columns: IParameter<*>,
        expression: Expression? = null,
        orderBy: List<String>? = null,
        limit: Int? = null,
        offset: Int? = null,
        block: suspend ResultSet.() -> T
    ): T {
        val sql = DatabaseVocabulary.selectSql(
            name, columns = columns.map { it.name },
            where = expression?.sqlString, orderBy = orderBy ?: emptyList(),
            limit = limit, offset = offset
        ).also { println("Select from $name:  $it") }
        return database.queryStatement(sql) {
            expression?.valueOrder?.forEachIndexed { index, value ->
                set(index + 1, value)
            }
            executeQuery().use { block() }
        }
    }

    suspend fun delete(
        expression: Expression? = null,
    ): Int {
        val sql = DatabaseVocabulary.deleteSql(
            name, expression?.sqlString
        ).also { println("Select from $name:  $it") }
        return database.updateStatement(sql) {
            expression?.valueOrder?.forEachIndexed { index, value ->
                set(index + 1, value)
            }
        }
    }

}