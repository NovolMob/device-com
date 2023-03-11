package ru.novolmob.jdbcdatabase.tables.parameters.types

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import java.math.BigDecimal
import java.sql.Types
import java.util.*

sealed class PrimitiveParameterType<T : Any>(override val databaseType: String, override val databaseTypeId: Int) : IParameterType<T> {
    override fun toDbType(value: T): Any = value
    override fun fromDbType(db: Any): T = db as T

    object Text: PrimitiveParameterType<String>(DatabaseVocabulary.TEXT, Types.VARCHAR)
    class Decimal(precision: Int, scale: Int):
        PrimitiveParameterType<BigDecimal>(DatabaseVocabulary.decimal(precision, scale), Types.DECIMAL)
    class VarChar(n: Int): PrimitiveParameterType<String>(DatabaseVocabulary.varchar(n), Types.NVARCHAR)
    object Integer: PrimitiveParameterType<Int>(DatabaseVocabulary.INT, Types.INTEGER)
    object Uuid: PrimitiveParameterType<UUID>(DatabaseVocabulary.UUID, Types.JAVA_OBJECT)
    object Date: PrimitiveParameterType<java.util.Date>(DatabaseVocabulary.DATE, Types.DATE)
    object Timestamp: PrimitiveParameterType<java.sql.Timestamp>(DatabaseVocabulary.TIMESTAMP, Types.TIMESTAMP)
    object Boolean: PrimitiveParameterType<kotlin.Boolean>(DatabaseVocabulary.BOOLEAN, Types.BOOLEAN)
}