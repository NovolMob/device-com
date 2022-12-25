package ru.novolmob.jdbcdatabase.extensions

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isChar
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isDecimal
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isNumeric
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary.isVarChar
import ru.novolmob.jdbcdatabase.extensions.DateExtension.toSql
import ru.novolmob.jdbcdatabase.tables.columns.IColumn
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue
import java.math.BigDecimal
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.ZoneId
import java.util.*

object PreparedStatementExtension {
    fun PreparedStatement.set(index: Int, value: ColumnValue<*>) =
        when (val db = value.dbValue) {
            is UUID -> setObject(index, db)
            is String -> setString(index, db)
            is Int -> setInt(index, db)
            is Short -> setShort(index, db)
            is Byte -> setByte(index, db)
            is Double -> setDouble(index, db)
            is Float -> setFloat(index, db)
            is BigDecimal -> setBigDecimal(index, db)
            is Date -> setDate(index, db)
            is java.util.Date -> setDate(index, db.toSql())
            is java.time.LocalDate -> setDate(index, Date.from(db.atStartOfDay(ZoneId.systemDefault()).toInstant()).toSql())
            is java.time.LocalDateTime -> setDate(index, Date.from(db.atZone(ZoneId.systemDefault()).toInstant()).toSql())
            is LocalDate -> setDate(index, Date.from(db.atStartOfDayIn(TimeZone.currentSystemDefault()).toJavaInstant()).toSql())
            is LocalDateTime -> setDate(index, Date.from(db.toInstant(TimeZone.currentSystemDefault()).toJavaInstant()).toSql())
            else -> throw Exception("${db::class} is not a primitive type!")
        }

    infix fun ResultSet.update(value: ColumnValue<*>) =
        when (val db = value.dbValue) {
            is UUID -> updateString(value.columnName, db.toString())
            is String -> updateString(value.columnName, db)
            is Int -> updateInt(value.columnName, db)
            is Short -> updateShort(value.columnName, db)
            is Byte -> updateByte(value.columnName, db)
            is Double -> updateDouble(value.columnName, db)
            is Float -> updateFloat(value.columnName, db)
            is Date -> updateDate(value.columnName, db)
            is BigDecimal -> updateBigDecimal(value.columnName, db)
            is java.util.Date -> updateDate(value.columnName, db.toSql())
            is java.time.LocalDate -> updateDate(value.columnName, Date.from(db.atStartOfDay(ZoneId.systemDefault()).toInstant()).toSql())
            is java.time.LocalDateTime -> updateDate(value.columnName, Date.from(db.atZone(ZoneId.systemDefault()).toInstant()).toSql())
            is LocalDate -> updateDate(value.columnName, Date.from(db.atStartOfDayIn(TimeZone.currentSystemDefault()).toJavaInstant()).toSql())
            is LocalDateTime -> updateDate(value.columnName, Date.from(db.toInstant(TimeZone.currentSystemDefault()).toJavaInstant()).toSql())
            else -> throw Exception("${db::class} is not a primitive type!")
        }

    inline infix fun <reified T: Any> ResultSet.get(column: IColumn<T>): T =
        column.type.databaseType.let {  type ->
            when {
                type == DatabaseVocabulary.TEXT -> getString(column.name)
                type.isVarChar() -> getString(column.name)
                type.isChar() -> getString(column.name)
                type.isDecimal() -> getBigDecimal(column.name)
                type.isNumeric() -> getBigDecimal(column.name)
                type == DatabaseVocabulary.INT -> getInt(column.name)
                type == DatabaseVocabulary.UUID -> getObject(column.name)
                type == DatabaseVocabulary.DATE -> getDate(column.name)
                type == DatabaseVocabulary.TIMESTAMP -> getTimestamp(column.name)
                else -> throw Exception("$type not found!")
            }.let(column.type::fromDbType)
        }

}

