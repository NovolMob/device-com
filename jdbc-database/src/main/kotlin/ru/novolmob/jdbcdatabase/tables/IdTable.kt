package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.set
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.use
import ru.novolmob.jdbcdatabase.tables.expressions.Expression
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.Column
import java.sql.ResultSet

abstract class IdTable<ID: Comparable<ID>>(name: String? = null): Table(name) {
    abstract val id: Column<ID>

    suspend fun delete(
        id: ID
    ) = delete(expression = this.id eq id)

    suspend fun <T> select(
        id: ID,
        block: suspend ResultSet.() -> T
    ) = select(expression = this.id eq id, block = block)

    suspend fun isExists(
        expression: Expression
    ): Boolean {
        val sql = DatabaseVocabulary.selectSql(
            columns = listOf(
                DatabaseVocabulary.exists(
                    DatabaseVocabulary.selectSql(where = expression.sqlString)
                )
            )
        ).also { println("Select from $name:  $it") }
        return database.queryStatement(sql) {
            expression.valueOrder.forEachIndexed { index, value ->
                set(index + 1, value)
            }
            executeQuery().use {
                next()
                getBoolean(1)
            }
        }
    }

}