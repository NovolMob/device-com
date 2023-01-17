package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.ParameterExtension.build
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.set
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.use
import ru.novolmob.jdbcdatabase.tables.parameters.FunctionParameter
import ru.novolmob.jdbcdatabase.tables.parameters.Parameterable
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue
import ru.novolmob.jdbcdatabase.utils.DatabaseObjectUtil.procedureName
import java.sql.CallableStatement
import java.sql.ResultSet

abstract class Function(
    name: String? = null,
    open val returnValue: Pair<String, Int>? = null,
    open val functionLanguage: DatabaseVocabulary.Language = DatabaseVocabulary.Language.SQL
): DatabaseObject(), Parameterable {
    override val name: String = name ?: javaClass.procedureName()
    private val _parameters = mutableListOf<FunctionParameter<*>>()
    abstract fun body(): String
    val parameters: List<FunctionParameter<*>>
        get() = _parameters.toList()

    override fun <T: Any> registerParameter(name: String, type: IParameterType<T>): FunctionParameter<T> =
        FunctionParameter(name, type, this).also { _parameters.add(it) }

    override suspend fun create() {
        database.statement(
            DatabaseVocabulary.createFunctionSql(
                name, parameters.build(),
                returnValue?.first, functionLanguage, body()
            ).also { println("Function $name created:  $it") }
        )
    }

    fun titleString(
        vararg parameters: String
    ) = DatabaseVocabulary.titleString(name, parameters.toList())

    suspend fun <T> callUpdate(vararg values: ParameterValue<*>, block: suspend CallableStatement.() -> T): T {
        val hasReturnValue = returnValue != null
        return database.callStatement(
            DatabaseVocabulary.callFunctionSql(name, hasReturnValue, values.size).also { println("Call $name:  $it") }
        ) {
            val startIndex = if (hasReturnValue) 2 else 1
            if (hasReturnValue) registerOutParameter(1, returnValue!!.second)
            values.forEachIndexed { index, value ->
                set(index + startIndex, value)
            }
            executeUpdate()
            block()
        }
    }

    suspend fun <T> callQuery(vararg values: ParameterValue<*>, block: suspend ResultSet.() -> T): T {
        val hasReturnValue = returnValue != null
        return database.callStatement(
            DatabaseVocabulary.callFunctionSql(name, hasReturnValue, values.size).also { println("Call $name:  $it") }
        ) {
            val startIndex = if (hasReturnValue) 2 else 1
            if (hasReturnValue) registerOutParameter(1, returnValue!!.second)
            values.forEachIndexed { index, value ->
                set(index + startIndex, value)
            }
            executeQuery().use { block() }
        }
    }

}