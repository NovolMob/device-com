package ru.novolmob.jdbcdatabase.views

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.set
import ru.novolmob.jdbcdatabase.tables.expressions.Expression
import ru.novolmob.jdbcdatabase.tables.columns.IColumn
import ru.novolmob.jdbcdatabase.utils.DatabaseObjectUtil.viewName
import java.sql.ResultSet

abstract class View(name: String? = null): DatabaseObject() {
    val name: String = name ?: javaClass.viewName()

    fun select(vararg columns: IColumn<*>, expression: Expression): ResultSet {
        val sql = DatabaseVocabulary.selectSql(name, columns = columns.map { it.name }, where = expression.sqlString).also { println("Select from $name:  $it") }
        return database.queryStatement(sql) {
            expression.valueOrder.forEachIndexed { index, value ->
                set(index + 1, value)
            }
        }
    }

}