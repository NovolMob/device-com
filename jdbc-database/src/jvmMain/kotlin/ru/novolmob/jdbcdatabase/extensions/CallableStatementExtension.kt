package ru.novolmob.jdbcdatabase.extensions

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isChar
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isDecimal
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isNumeric
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isVarChar
import ru.novolmob.jdbcdatabase.tables.parameters.types.IParameterType
import java.sql.CallableStatement

object CallableStatementExtension {

    fun <T> CallableStatement.get(index: Int, type: IParameterType<T>): T? =
        type.databaseType.let { typeString ->
            when {
                typeString == DatabaseVocabulary.TEXT -> getString(index)
                typeString.isVarChar() -> getString(index)
                typeString.isChar() -> getString(index)
                typeString.isDecimal() -> getBigDecimal(index)
                typeString.isNumeric() -> getBigDecimal(index)
                typeString == DatabaseVocabulary.BOOLEAN -> getBoolean(index)
                typeString == DatabaseVocabulary.INT -> getInt(index)
                typeString == DatabaseVocabulary.UUID -> getObject(index)
                typeString == DatabaseVocabulary.DATE -> getDate(index)
                typeString == DatabaseVocabulary.TIMESTAMP -> getTimestamp(index)
                else -> throw Exception("$typeString not found!")
            }.let {
                if (it == null) null
                else  type.fromDbType(it)
            }
        }

}