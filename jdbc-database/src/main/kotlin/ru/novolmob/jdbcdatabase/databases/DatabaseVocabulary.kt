package ru.novolmob.jdbcdatabase.databases

import ru.novolmob.jdbcdatabase.extensions.StringExtension.ifNotInEnd

object DatabaseVocabulary {
    const val TEXT = "text"
    private const val CHAR = "char"
    private const val VARCHAR = "char"
    private const val NUMERIC = "numeric"
    private const val DECIMAL = "decimal"
    const val INT = "int"
    const val UUID = "uuid"
    const val DATE = "date"
    const val TIMESTAMP = "timestamp"

    const val IF_NOT_EXISTS = "IF NOT EXISTS"
    private const val DEFAULT = "DEFAULT"
    const val PRIMARY_KEY = "PRIMARY KEY"
    const val DISTINCT_ON = "DISTINCT ON"
    const val FOREIGN_KEY = "FOREIGN KEY"
    const val REFERENCES = "REFERENCES"
    const val CONSTRAINT = "CONSTRAINT"
    const val OR_REPLACE = "OR REPLACE"
    const val RESTRICT = "RESTRICT"
    const val NOT_NULL = "NOT NULL"
    const val ORDER_BY = "ORDER BY"
    const val CASCADE = "CASCADE"
    const val BETWEEN = "BETWEEN"
    const val UNIQUE = "UNIQUE"
    const val VALUES = "VALUES"
    const val WHERE = "WHERE"
    const val RIGHT = "RIGHT"
    const val CROSS = "CROSS"
    const val INTO = "INTO"
    const val FROM = "FROM"
    const val JOIN = "JOIN"
    const val LEFT = "LEFT"
    const val NULL = "NULL"
    const val AND = "AND"
    const val SET = "SET"
    const val OR = "OR"
    const val ON = "ON"
    const val AS = "AS"

    const val CREATE = "CREATE"
    const val UPDATE = "UPDATE"
    const val SELECT = "SELECT"
    const val ALTER = "ALTER"
    const val INSERT = "INSERT"
    const val DELETE = "DELETE"

    const val TABLE = "TABLE"
    const val COLUMN = "COLUMN"
    const val VIEW = "VIEW"

    const val UUID_GENERATION = "uuid_generate_v4()"
    const val UPDATE_TIME_GENERATION = "current_timestamp"
    const val CREATION_TIME_GENERATION = "current_timestamp"

    const val ON_UPDATE_RESTRICT = "$ON $UPDATE $RESTRICT"
    const val ON_DELETE_RESTRICT = "$ON $DELETE $RESTRICT"
    const val ON_UPDATE_CASCADE = "$ON $UPDATE $CASCADE"
    const val ON_DELETE_CASCADE = "$ON $DELETE $CASCADE"

    fun numeric(precision: Int, scale: Int) = "$NUMERIC($precision, $scale)"
    fun String.isNumeric() = startsWith(NUMERIC)
    fun decimal(precision: Int, scale: Int) = "$DECIMAL($precision, $scale)"
    fun String.isDecimal() = startsWith(DECIMAL)
    fun varchar(n: Int) = "$VARCHAR($n)"
    fun String.isVarChar() = startsWith(VARCHAR)
    fun char(n: Int) = "$CHAR($n)"
    fun String.isChar() = startsWith(CHAR)
    fun default(sql: String) = "$DEFAULT $sql"
    fun join(table: String, firstTableAndParam: String, secondTableAndParam: String) =
        "$JOIN $table $ON $firstTableAndParam $EQUAL_SYMBOL $secondTableAndParam"
    fun rightJoin(table: String, firstTableAndParam: String, secondTableAndParam: String) =
        "$RIGHT " + join(table, firstTableAndParam, secondTableAndParam)
    fun leftJoin(table: String, firstTableAndParam: String, secondTableAndParam: String) =
        "$LEFT " + join(table, firstTableAndParam, secondTableAndParam)
    fun crossJoin(table: String, firstTableAndParam: String, secondTableAndParam: String) =
        "$CROSS " + join(table, firstTableAndParam, secondTableAndParam)

    const val PARAMETER_SYMBOL = "?"
    const val EQUAL_SYMBOL = "="
    const val LESS_EQUAL_SYMBOL = "<="
    const val LESS_SYMBOL = "<"
    const val MORE_EQUAL_SYMBOL = ">="
    const val MORE_SYMBOL = ">"
    const val ALL_COLUMNS_SYMBOL = "*"

    fun createTableSql(
        tableName: String,
        columns: List<String>
    ): String =
        "$CREATE $TABLE $IF_NOT_EXISTS $tableName (${columns.joinToString()});"

    fun createViewSql(
        name: String,
        request: String
    ): String =
        "$CREATE $OR_REPLACE $VIEW $name $AS $request".ifNotInEnd(";")

    fun insertSql(
        tableName: String,
        columns: List<String>
    ): String =
        "$INSERT $INTO $tableName (${columns.joinToString()}) $VALUES (${List(columns.size) {PARAMETER_SYMBOL}.joinToString()});"

    fun updateSql(
        tableName: String,
        columns: List<String>,
        where: String? = null
    ): String =
        "$UPDATE $tableName $SET ${columns.joinToString { "$it = $PARAMETER_SYMBOL" }}${ where?.let { " $WHERE $it" } ?: "" };"

    fun selectSql(
        from: String,
        distinct: List<String> = emptyList(),
        columns: List<String> = emptyList(),
        joins: List<String> = emptyList(),
        where: String? = null,
        orderBy: List<String> = emptyList()
    ) =
        "$SELECT" +
                "${ distinct.takeIf { it.isNotEmpty() }?.let { " $DISTINCT_ON (${it.joinToString()})" } ?: "" }" +
                " ${ columns.takeIf { it.isNotEmpty() }?.joinToString() ?: ALL_COLUMNS_SYMBOL }" +
                " $FROM $from" +
                "${ joins.takeIf { it.isNotEmpty() }?.joinToString(" ", prefix = " ") ?: "" }" +
                "${ where?.let { " $WHERE $it" } ?: "" }" +
                "${ orderBy.takeIf { it.isNotEmpty() }?.let { " $ORDER_BY ${it.joinToString()}" } ?: "" };"

}