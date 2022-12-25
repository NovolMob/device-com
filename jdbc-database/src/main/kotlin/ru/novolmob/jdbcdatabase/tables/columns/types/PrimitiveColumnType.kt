package ru.novolmob.jdbcdatabase.tables.columns.types

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.math.BigDecimal

sealed class PrimitiveColumnType<T : Any>(override val databaseType: String) : IColumnType<T> {
    override fun toDbType(value: T): Any = value
    override fun fromDbType(db: Any): T = db as T

    object Text: PrimitiveColumnType<String>(DatabaseVocabulary.TEXT)
    class Decimal(precision: Int, scale: Int):
        PrimitiveColumnType<BigDecimal>(DatabaseVocabulary.decimal(precision, scale))
    class VarChar(n: Int): PrimitiveColumnType<String>(DatabaseVocabulary.varchar(n))
    object Integer: PrimitiveColumnType<Int>(DatabaseVocabulary.INT)
    object Uuid: PrimitiveColumnType<Uuid>(DatabaseVocabulary.UUID)
    object Date: PrimitiveColumnType<Date>(DatabaseVocabulary.DATE)
    object LocalDate: PrimitiveColumnType<LocalDate>(DatabaseVocabulary.DATE)
    object LocalDateTime: PrimitiveColumnType<LocalDateTime>(DatabaseVocabulary.TIMESTAMP)
}