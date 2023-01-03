package ru.novolmob.exposeddatabase.columntypes

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IDateColumnType
import org.jetbrains.exposed.sql.vendors.*
import ru.novolmob.core.models.DateTimed
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class CustomDateTimeColumnType<T: DateTimed>(
    val constructor: (LocalDateTime) -> T,
): ColumnType(), IDateColumnType {
    override val hasTimePart: Boolean = true
    override fun sqlType(): String = currentDialect.dataTypeProvider.dateTimeType()

    override fun nonNullValueToString(value: Any): String {
        val instant = when (value) {
            is DateTimed -> value.dateTime.toInstant(DEFAULT_TIME_ZONE)
            is String -> return value
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

    override fun valueFromDB(value: Any): Any = when (value) {
        is DateTimed -> value
        is LocalDateTime -> value.let(constructor)
        is java.sql.Date -> longToLocalDateTime(value.time).let(constructor)
        is java.sql.Timestamp -> longToLocalDateTime(value.time / MILLIS_IN_SECOND, value.nanos.toLong()).let(constructor)
        is Int -> longToLocalDateTime(value.toLong()).let(constructor)
        is Long -> longToLocalDateTime(value).let(constructor)
        is java.time.LocalDateTime -> value.toKotlinLocalDateTime().let(constructor)
        is String -> java.time.LocalDateTime.parse(value, formatterForDateString(value)).toKotlinLocalDateTime().let(constructor)
        else -> valueFromDB(value.toString())
    }

    override fun notNullValueToDB(value: Any): Any = when {
        value is DateTimed && currentDialect is SQLiteDialect ->
            SQLITE_AND_ORACLE_DATE_TIME_STRING_FORMATTER.format(value.dateTime.toJavaLocalDateTime().atZone(ZoneId.systemDefault()))
        value is LocalDateTime && currentDialect is SQLiteDialect ->
            SQLITE_AND_ORACLE_DATE_TIME_STRING_FORMATTER.format(value.toJavaLocalDateTime().atZone(ZoneId.systemDefault()))
        value is LocalDateTime -> {
            val instant = value.toJavaLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()
            java.sql.Timestamp(instant.toEpochMilli()).apply { nanos = instant.nano }
        }
        value is DateTimed -> {
            val instant = value.dateTime.toJavaLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()
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