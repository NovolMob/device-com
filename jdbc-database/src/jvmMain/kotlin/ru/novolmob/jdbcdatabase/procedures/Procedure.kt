package ru.novolmob.jdbcdatabase.procedures

import ru.novolmob.jdbcdatabase.databases.DatabaseObject
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.CallableStatementExtension.get
import ru.novolmob.jdbcdatabase.extensions.ParameterExtension.build
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.set
import ru.novolmob.jdbcdatabase.tables.parameters.Parameterable
import ru.novolmob.jdbcdatabase.tables.parameters.ProcedureParameter
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType
import ru.novolmob.jdbcdatabase.tables.parameters.values.ProcedureParameterValue
import ru.novolmob.jdbcdatabase.utils.DatabaseObjectUtil.procedureName

abstract class Procedure(
    name: String? = null,
    open val procedureLanguage: DatabaseVocabulary.Language = DatabaseVocabulary.Language.SQL
): DatabaseObject(), Parameterable {
    override val name: String = name ?: javaClass.procedureName()
    private val _parameters = mutableListOf<ProcedureParameter<*>>()
    abstract val body: String
    val parameters: List<ProcedureParameter<*>>
        get() = _parameters.toList()

    override fun <T: Any> registerParameter(name: String, type: IParameterType<T>): ProcedureParameter<T> =
        ProcedureParameter(name, type, this).also { _parameters.add(it) }

    override suspend fun create() {
        database.statement(
            DatabaseVocabulary.createProcedureSql(name, parameters.build(), procedureLanguage, body).also { println("Procedure $name created:  $it") }
        )
    }

    fun titleString(
        vararg parameters: String
    ) = DatabaseVocabulary.titleString(name, parameters.toList())

    suspend fun call(vararg values: ProcedureParameterValue<*>): List<*> {
        val outer = values.filter { it.isOut() }
        return database.callStatement(
            DatabaseVocabulary.callProcedureSql(
                name,
                values.size
            ).also { println("Call $name:  $it") }
        ) {
            outer.forEachIndexed { index, value ->
                registerOutParameter(index + 1, value.type.databaseTypeId)
            }
            values.forEachIndexed { index, value ->
                set(index + 1, value)
            }
            executeUpdate()
            outer.mapIndexed { index, value ->
                get(index + 1, value.type)
            }
        }
    }

}