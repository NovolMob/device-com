package ru.novolmob.exposeddatabase.columntypes

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IDateColumnType
import org.jetbrains.exposed.sql.vendors.SQLiteDialect
import org.jetbrains.exposed.sql.vendors.currentDialect
import ru.novolmob.core.models.Dated
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class CustomDateColumnType<T: Dated>(
    val constructor: (LocalDate) -> T,
): ColumnType(), IDateColumnType {
    override val hasTimePart: Boolean = false

    override fun sqlType(): String = "DATE"

    override fun nonNullValueToString(value: Any): String {
        val instant = when (value) {
            is Dated -> Instant.fromEpochMilliseconds(value.date.atStartOfDayIn(DEFAULT_TIME_ZONE).toEpochMilliseconds())
            is String -> return value
            is LocalDate -> Instant.fromEpochMilliseconds(value.atStartOfDayIn(DEFAULT_TIME_ZONE).toEpochMilliseconds())
            is java.sql.Date -> Instant.fromEpochMilliseconds(value.time)
            is java.sql.Timestamp -> Instant.fromEpochSeconds(value.time / MILLIS_IN_SECOND, value.nanos.toLong())
            else -> error("Unexpected value: $value of ${value::class.qualifiedName}")
        }

        return "'${DEFAULT_DATE_STRING_FORMATTER.format(instant.toJavaInstant())}'"
    }

    override fun valueFromDB(value: Any): Any = when (value) {
        is Dated -> value
        is LocalDate -> value.let(constructor)
        is java.sql.Date -> longToLocalDate(value.time).let(constructor)
        is java.sql.Timestamp -> longToLocalDate(value.time).let(constructor)
        is Int -> longToLocalDate(value.toLong()).let(constructor)
        is Long -> longToLocalDate(value).let(constructor)
        is String -> when (currentDialect) {
            is SQLiteDialect -> LocalDate.parse(value).let(constructor)
            else -> value
        }
        else -> LocalDate.parse(value.toString()).let(constructor)
    }

    override fun notNullValueToDB(value: Any) = when (value) {
        is Dated -> java.sql.Date(value.date.millis)
        is LocalDate -> java.sql.Date(value.millis)
        else -> value
    }

    private fun longToLocalDate(instant: Long) = Instant.fromEpochMilliseconds(instant).toLocalDateTime(
        DEFAULT_TIME_ZONE
    ).date

    companion object {
        private const val MILLIS_IN_SECOND = 1000

        private val DEFAULT_TIME_ZONE by lazy {
            TimeZone.currentSystemDefault()
        }

        private val LocalDate.millis get() = this.atStartOfDayIn(TimeZone.currentSystemDefault()).epochSeconds * MILLIS_IN_SECOND

        private val DEFAULT_DATE_STRING_FORMATTER by lazy {
            DateTimeFormatter.ISO_LOCAL_DATE.withLocale(Locale.ROOT).withZone(ZoneId.systemDefault())
        }
    }
}