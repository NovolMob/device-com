package ru.novolmob.jdbcdatabase.extensions

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isChar
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isDecimal
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isNumeric
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isVarChar
import ru.novolmob.jdbcdatabase.extensions.DateExtension.toSql
import ru.novolmob.jdbcdatabase.tables.parameters.IParameter
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue
import java.math.BigDecimal
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import java.time.ZoneId
import java.util.*

object PreparedStatementExtension {
    fun PreparedStatement.set(index: Int, value: ParameterValue<*>) =
        when (val db = value.dbValue) {
            is UUID -> setObject(index, db)
            is ru.novolmob.core.models.ids.UUID -> setObject(index, db.javaUUID)
            is String -> setString(index, db)
            is Boolean -> setBoolean(index, db)
            is Int -> setInt(index, db)
            is Short -> setShort(index, db)
            is Byte -> setByte(index, db)
            is Double -> setDouble(index, db)
            is Float -> setFloat(index, db)
            is BigDecimal -> setBigDecimal(index, db)
            is ru.novolmob.core.models.BigDecimal -> setBigDecimal(index, db.javaBigDecimal)
            is Date -> setDate(index, db)
            is java.util.Date -> setDate(index, db.toSql())
            is java.time.LocalDate -> setDate(index, Date.from(db.atStartOfDay(ZoneId.systemDefault()).toInstant()).toSql())
            is java.time.LocalDateTime -> setDate(index, Date.from(db.atZone(ZoneId.systemDefault()).toInstant()).toSql())
            is LocalDate -> setDate(index, Date.from(db.atStartOfDayIn(TimeZone.currentSystemDefault()).toJavaInstant()).toSql())
            is LocalDateTime -> setDate(index, Date.from(db.toInstant(TimeZone.currentSystemDefault()).toJavaInstant()).toSql())
            else -> {
                if (db == null) setNull(index, Types.OTHER)
                else throw Exception("${value.type.databaseTypeId} ${value.parameterName} is not a primitive type!")
            }
        }

    inline fun <R> ResultSet.use(block: ResultSet.() -> R): R =
        use { it: ResultSet ->
            it.run { block() }
        }

    infix fun ResultSet.update(value: ParameterValue<*>) =
        when (val db = value.dbValue) {
            is UUID -> updateString(value.parameterName, db.toString())
            is ru.novolmob.core.models.ids.UUID -> updateString(value.parameterName, db.toString())
            is String -> updateString(value.parameterName, db as String?)
            is Boolean -> updateBoolean(value.parameterName, db)
            is Int -> updateInt(value.parameterName, db)
            is Short -> updateShort(value.parameterName, db)
            is Byte -> updateByte(value.parameterName, db)
            is Double -> updateDouble(value.parameterName, db)
            is Float -> updateFloat(value.parameterName, db)
            is Date -> updateDate(value.parameterName, db)
            is BigDecimal -> updateBigDecimal(value.parameterName, db)
            is ru.novolmob.core.models.BigDecimal -> updateBigDecimal(value.parameterName, db.javaBigDecimal)
            is java.util.Date -> updateDate(value.parameterName, db.toSql())
            is java.time.LocalDate -> updateDate(value.parameterName, Date.from(db.atStartOfDay(ZoneId.systemDefault()).toInstant()).toSql())
            is java.time.LocalDateTime -> updateDate(value.parameterName, Date.from(db.atZone(ZoneId.systemDefault()).toInstant()).toSql())
            is LocalDate -> updateDate(value.parameterName, Date.from(db.atStartOfDayIn(TimeZone.currentSystemDefault()).toJavaInstant()).toSql())
            is LocalDateTime -> updateDate(value.parameterName, Date.from(db.toInstant(TimeZone.currentSystemDefault()).toJavaInstant()).toSql())
            else -> {
                if (db == null) updateNull(value.parameterName)
                else throw Exception("${value.type.databaseTypeId} ${value.parameterName} is not a primitive type!")
            }
        }

    infix fun <T: Any> ResultSet.get(parameter: IParameter<T>): T =
        getOrNull(parameter) ?: throw Exception("${parameter.name} not found")

    infix fun <T: Any> ResultSet.getOrNull(parameter: IParameter<T>): T? =
        parameter.type.databaseType.let { type ->
            when {
                type == DatabaseVocabulary.TEXT -> getString(parameter.name)
                type.isVarChar() -> getString(parameter.name)
                type.isChar() -> getString(parameter.name)
                type.isDecimal() -> getBigDecimal(parameter.name)
                type.isNumeric() -> getBigDecimal(parameter.name)
                type == DatabaseVocabulary.BOOLEAN -> getBoolean(parameter.name)
                type == DatabaseVocabulary.INT -> getInt(parameter.name)
                type == DatabaseVocabulary.UUID -> getObject(parameter.name)
                type == DatabaseVocabulary.DATE -> getDate(parameter.name)
                type == DatabaseVocabulary.TIMESTAMP -> getTimestamp(parameter.name)
                else -> throw Exception("$type not found!")
            }.let {
                if (it == null) null
                else  parameter.type.fromDbType(it)
            }
        }

}

