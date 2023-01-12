package ru.novolmob.jdbcdatabase.views

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.ParameterExtension.build
import ru.novolmob.jdbcdatabase.extensions.ParameterExtension.fullName
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.set
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.use
import ru.novolmob.jdbcdatabase.tables.expressions.Expression
import ru.novolmob.jdbcdatabase.tables.parameters.IParameter
import ru.novolmob.jdbcdatabase.tables.parameters.ViewParameter
import ru.novolmob.jdbcdatabase.utils.DatabaseObjectUtil.viewName
import java.sql.ResultSet

abstract class View(name: String? = null): DatabaseObject() {
    override val name: String = name ?: javaClass.viewName()
    private val _parameters = mutableListOf<ViewParameter<*>>()
    val parameters: List<ViewParameter<*>>
        get() = _parameters.toList()
    private val _joins = mutableListOf<Pair<IParameter<*>, IParameter<*>>>()
    val joins
        get() = _joins.toList()

    protected fun <T: Any> registerJoin(first: IParameter<T>, second: IParameter<T>): Pair<IParameter<T>, IParameter<T>> =
        (first to second).also { _joins.add(it) }
    protected fun <T : Any> registerParameter(name: String? = null, reference: IParameter<T>): ViewParameter<T> =
        ViewParameter(name, reference, this).also { _parameters.add(it); println("${this.name}  ${it.name}  ${_parameters.size}") }

    override suspend fun create() {
        val sql = DatabaseVocabulary.createViewSql(
            name,
            DatabaseVocabulary.selectSql(
                (parameters.map { it.reference.databaseObject.name }.distinct().toSet() - (joins.map { it.second.databaseObject.name }.distinct().toSet())).first(),
                columns = parameters.build(),
                joins = joins.map { DatabaseVocabulary.join(it.second.databaseObject.name, it.first.fullName(), it.second.fullName()) }
            )
        ).also { println("View $name created:  $it") }
        database.statement(sql)
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

}