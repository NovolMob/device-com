package ru.novolmob.database.columntypes

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IDateColumnType
import org.jetbrains.exposed.sql.vendors.*
import ru.novolmob.database.models.Dated
import java.sql.ResultSet
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class CustomDateColumnType<T: Dated>(
    private val constructor: (LocalDateTime) -> T,
): ColumnType(), IDateColumnType {
    override val hasTimePart: Boolean = true
    override fun sqlType(): String = currentDialect.dataTypeProvider.dateTimeType()

    override fun readObject(rs: ResultSet, index: Int): Any? {
        return rs.getDate(index)?.time?.let(::longToLocalDateTime)?.let(constructor)
    }

    override fun nonNullValueToString(value: Any): String {
        val instant = when (value) {
            is String -> return value
            is Dated -> value.date.toInstant(DEFAULT_TIME_ZONE)
            is LocalDateTime -> value.toInstant(DEFAULT_TIME_ZONE)
            is java.sql.Date -> Instant.fromEpochMilliseconds(value.time)
            is java.sql.Timestamp -> Instant.fromEpochSeconds(value.time / MILLIS_IN_SECOND, value.nanos.toLong())
            else -> error("Unexpected value: $value of ${value::class.qualifiedName}")
        }

        val dialect = currentDialect

        return when {
            dialect is SQLiteDialect -> "'${SQLITE_AND_ORACLE_DATE_TIME_STRING_FORMATTER.format(instant.toJavaInstant())}'"
            dialect is OracleDialect || dialect.h2Mode == H2Dialect.H2CompatibilityMode.Oracle ->
                "'${SQLITE_AND_ORACLE_DATE_TIME_STRING_FORMATTER.format(instant.toJavaInstant())}'"
            else -> "'${DEFAULT_DATE_TIME_STRING_FORMATTER.format(instant.toJavaInstant())}'"
        }
    }

    override fun valueFromDB(value: Any): Any {
        val instant = when (value) {
            is LocalDateTime -> value
            is java.sql.Date -> longToLocalDateTime(value.time)
            is java.sql.Timestamp -> longToLocalDateTime(value.time / MILLIS_IN_SECOND, value.nanos.toLong())
            is Int -> longToLocalDateTime(value.toLong())
            is Long -> longToLocalDateTime(value)
            is java.time.LocalDateTime -> value.toKotlinLocalDateTime()
            is String -> java.time.LocalDateTime.parse(value, formatterForDateString(value)).toKotlinLocalDateTime()
            else -> valueFromDB(value.toString())
        }

        return when(instant) {
            is LocalDateTime -> constructor(instant)
            else -> instant
        }
    }

    override fun notNullValueToDB(value: Any): Any = when {
        value is LocalDateTime && currentDialect is SQLiteDialect ->
            SQLITE_AND_ORACLE_DATE_TIME_STRING_FORMATTER.format(value.toJavaLocalDateTime().atZone(ZoneId.systemDefault()))
        value is LocalDateTime -> {
            val instant = value.toJavaLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()
            java.sql.Timestamp(instant.toEpochMilli()).apply { nanos = instant.nano }
        }
        else -> value
    }

    private fun longToLocalDateTime(millis: Long) = Instant.fromEpochMilliseconds(millis).toLocalDateTime(
        DEFAULT_TIME_ZONE
    )
    private fun longToLocalDateTime(seconds: Long, nanos: Long) = Instant.fromEpochSeconds(seconds, nanos).toLocalDateTime(
        DEFAULT_TIME_ZONE
    )

    companion object {
        private const val MILLIS_IN_SECOND = 1000

        private val DEFAULT_TIME_ZONE by lazy {
            TimeZone.currentSystemDefault()
        }
        private val DEFAULT_DATE_TIME_STRING_FORMATTER by lazy {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.withLocale(Locale.ROOT).withZone(ZoneId.systemDefault())
        }
        private val SQLITE_AND_ORACLE_DATE_TIME_STRING_FORMATTER by lazy {
            DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss.SSS",
                Locale.ROOT
            ).withZone(ZoneId.systemDefault())
        }

        private fun formatterForDateString(date: String) = dateTimeWithFractionFormat(date.substringAfterLast('.', "").length)
        private fun dateTimeWithFractionFormat(fraction: Int): DateTimeFormatter {
            val baseFormat = "yyyy-MM-d HH:mm:ss"
            val newFormat = if (fraction in 1..9) {
                (1..fraction).joinToString(prefix = "$baseFormat.", separator = "") { "S" }
            } else {
                baseFormat
            }
            return DateTimeFormatter.ofPattern(newFormat).withLocale(Locale.ROOT).withZone(ZoneId.systemDefault())
        }
    }
}