package ru.novolmob.database.tables.credentials

import org.jetbrains.exposed.sql.ColumnType
import ru.novolmob.database.utils.PhoneNumberUtil
import java.sql.Clob

class PhoneNumberColumnType: ColumnType() {
    override fun sqlType(): String = "TEXT"

    override fun valueFromDB(value: Any): Any =
        when (value) {
            is Clob -> PhoneNumberUtil.deserializePhoneNumber(value.characterStream.readText()) ?: value
            is ByteArray -> PhoneNumberUtil.deserializePhoneNumber(String(value)) ?: value
            else -> value
        }
}