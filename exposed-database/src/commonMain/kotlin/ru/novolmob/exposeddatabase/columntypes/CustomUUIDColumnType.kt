package ru.novolmob.exposeddatabase.columntypes

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.vendors.MariaDBDialect
import org.jetbrains.exposed.sql.vendors.currentDialect
import ru.novolmob.core.models.ids.UUID
import ru.novolmob.core.models.ids.UUIDable
import java.nio.ByteBuffer
import java.sql.ResultSet

class CustomUUIDColumnType<T: UUIDable>(
    val constructor: (UUID) -> T
): ColumnType() {
    override fun sqlType(): String = currentDialect.dataTypeProvider.uuidType()

    override fun valueFromDB(value: Any): T = when {
        value is UUIDable -> value.uuid
        value is UUID -> value
        value is java.util.UUID -> UUID(value)
        value is ByteArray -> ByteBuffer.wrap(value).let { b -> UUID(b.long, b.long) }
        value is String && value.matches(uuidRegexp) -> UUID.fromString(value)
        value is String -> ByteBuffer.wrap(value.toByteArray()).let { b -> UUID(b.long, b.long) }
        else -> error("Unexpected value of type UUID: $value of ${value::class.qualifiedName}")
    }.let(constructor)

    override fun notNullValueToDB(value: Any): Any = currentDialect.dataTypeProvider.uuidToDB(valueToUUID(value).javaUUID)

    override fun nonNullValueToString(value: Any): String = "'${valueToUUID(value)}'"

    private fun valueToUUID(value: Any): UUID = when (value) {
        is UUID -> value
        is UUIDable -> value.uuid
        is java.util.UUID -> UUID(value)
        is String -> UUID.fromString(value)
        is ByteArray -> ByteBuffer.wrap(value).let { UUID(it.long, it.long) }
        else -> error("Unexpected value of type UUID: ${value.javaClass.canonicalName}")
    }

    override fun readObject(rs: ResultSet, index: Int): Any? = when (currentDialect) {
        is MariaDBDialect -> rs.getBytes(index)
        else -> super.readObject(rs, index)
    }

    companion object {
        private val uuidRegexp = Regex("[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}", RegexOption.IGNORE_CASE)
    }
}