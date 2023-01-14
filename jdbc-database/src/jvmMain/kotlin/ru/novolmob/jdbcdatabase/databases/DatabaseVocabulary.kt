package ru.novolmob.jdbcdatabase.databases

import ru.novolmob.jdbcdatabase.extensions.StringExtension.ifNotInEnd

object DatabaseVocabulary {

    enum class Language(val sql: String) {
        SQL(SQL_LANGUAGE),
        PLPGSQL(PLPGSQL_LANGUAGE);
    }

    const val UNIQUE: String = "UNIQUE"
    const val NULL: String = "NULL"
    const val NOT_NULL: String = "NOT NULL"

    const val TEXT = "text"
    private const val CHAR = "char"
    private const val VARCHAR = "char"
    private const val NUMERIC = "numeric"
    private const val DECIMAL = "decimal"
    const val INT = "int"
    const val UUID = "uuid"
    const val VOID = "void"
    const val BOOLEAN = "boolean"
    const val REFCURSOR = "refcursor"
    const val DATE = "date"
    const val TIMESTAMP = "timestamp"

    const val UUID_GENERATION = "uuid_generate_v4()"
    const val UPDATE_TIME_GENERATION = "current_timestamp"
    const val CREATION_TIME_GENERATION = "current_timestamp"

    const val LANGUAGE = "LANGUAGE"
    private const val SQL_LANGUAGE = "sql"
    private const val PLPGSQL_LANGUAGE = "plpgsql"

    const val ON_UPDATE_RESTRICT = "ON UPDATE RESTRICT"
    const val ON_DELETE_RESTRICT = "ON DELETE RESTRICT"
    const val ON_UPDATE_CASCADE = "ON UPDATE CASCADE"
    const val ON_DELETE_CASCADE = "ON DELETE CASCADE"

    fun numeric(precision: Int, scale: Int) = "$NUMERIC($precision, $scale)"
    fun String.isNumeric() = startsWith(NUMERIC)
    fun decimal(precision: Int, scale: Int) = "$DECIMAL($precision, $scale)"
    fun String.isDecimal() = startsWith(DECIMAL)
    fun varchar(n: Int) = "$VARCHAR($n)"
    fun String.isVarChar() = startsWith(VARCHAR)
    fun char(n: Int) = "$CHAR($n)"
    fun String.isChar() = startsWith(CHAR)
    fun default(sql: String) = "DEFAULT $sql"
    fun exists(sql: String) = "exists($sql)"
    fun asNewName(parameter: String, newName: String) = "$parameter AS $newName"
    fun foreignKey(constraintName: String, parameterName: String, references: String, properties: List<String> = emptyList()): String =
        "CONSTRAINT $constraintName FOREIGN KEY ($parameterName) REFERENCES $references" +
                (properties.takeIf { it.isNotEmpty() }?.joinToString(" ", prefix = " ") ?: "")
    fun between(parameter: String, ): String = "$parameter BETWEEN $PARAMETER_SYMBOL AND $PARAMETER_SYMBOL"
    fun join(table: String, firstTableAndParam: String, secondTableAndParam: String) =
        "JOIN $table ON $firstTableAndParam $EQUAL_SYMBOL $secondTableAndParam"
    fun rightJoin(table: String, firstTableAndParam: String, secondTableAndParam: String) =
        "RIGHT " + join(table, firstTableAndParam, secondTableAndParam)
    fun leftJoin(table: String, firstTableAndParam: String, secondTableAndParam: String) =
        "LEFT " + join(table, firstTableAndParam, secondTableAndParam)
    fun crossJoin(table: String, firstTableAndParam: String, secondTableAndParam: String) =
        "CROSS " + join(table, firstTableAndParam, secondTableAndParam)

    const val PARAMETER_SYMBOL = "?"
    const val EQUAL_SYMBOL = "="
    const val LESS_EQUAL_SYMBOL = "<="
    const val LESS_SYMBOL = "<"
    const val MORE_EQUAL_SYMBOL = ">="
    const val MORE_SYMBOL = ">"
    const val ALL_COLUMNS_SYMBOL = "*"

    fun createTableSql(
        tableName: String,
        columns: List<String>,
        primaryKeys: List<String>,
    ): String =
        "CREATE TABLE IF NOT EXISTS $tableName (${ columns.joinToString() }${ primaryKeys.takeIf { it.isNotEmpty() }?.let { ", PRIMARY KEY (${it.joinToString()})" } ?: "" });"

    fun createViewSql(
        name: String,
        request: String
    ): String =
        "CREATE OR REPLACE VIEW $name AS $request".ifNotInEnd(";")

    fun createProcedureSql(
        name: String,
        parameters: List<String>,
        language: Language,
        body: String
    ): String =
        "CREATE OR REPLACE PROCEDURE $name(${parameters.joinToString()}) $LANGUAGE ${language.sql} AS $$ $body $$;"

    fun createFunctionSql(
        name: String,
        parameters: List<String>,
        returnValue: String? = null,
        language: Language,
        body: String
    ): String =
        "CREATE OR REPLACE FUNCTION $name(${parameters.joinToString()}) RETURNS ${returnValue ?: VOID} AS $$ $body $$ $LANGUAGE ${language.sql};"

    fun callProcedureSql(
        procedureName: String,
        amountOfParameters: Int = 0,
    ): String {
        val parameters = (if (amountOfParameters > 0) List(amountOfParameters) { PARAMETER_SYMBOL } else emptyList() )
        return "CALL ${titleString(procedureName, parameters)}"
    }

    fun callFunctionSql(
        functionName: String,
        hasReturnValue: Boolean,
        amountOfParameters: Int = 0,
    ): String {
        val parameters = (if (amountOfParameters > 0) List(amountOfParameters) { PARAMETER_SYMBOL } else emptyList() )
        return "{${if (hasReturnValue) " $PARAMETER_SYMBOL =" else ""} CALL ${titleString(functionName, parameters)} }"
    }

    fun titleString(
        name: String,
        parameters: List<String>,
    ): String = "$name( ${parameters.joinToString()} )"

    fun insertSql(
        tableName: String,
        columns: List<String>
    ): String =
        "INSERT INTO $tableName (${columns.joinToString()}) VALUES (${List(columns.size) {PARAMETER_SYMBOL}.joinToString()});"

    fun updateSql(
        tableName: String,
        columns: List<String>,
        where: String? = null
    ): String =
        "UPDATE $tableName SET ${columns.joinToString { "$it = $PARAMETER_SYMBOL" }}${ where?.let { " WHERE $it" } ?: "" };"

    fun deleteSql(
        from: String,
        where: String? = null
    ) =
        "SELECT FROM $from${ where?.let { " WHERE $it" } ?: "" };"

    fun selectSql(
        from: String? = null,
        distinct: List<String> = emptyList(),
        columns: List<String> = emptyList(),
        joins: List<String> = emptyList(),
        where: String? = null,
        groupBy: List<String> = emptyList(),
        orderBy: List<String> = emptyList(),
        limit: Int? = null,
        offset: Int? = null
    ) =
        "SELECT " +
                (distinct.takeIf { it.isNotEmpty() }?.let { "DISTINCT ON (${it.joinToString()})" } ?: "") +
                (columns.takeIf { it.isNotEmpty() }?.joinToString() ?: ALL_COLUMNS_SYMBOL) +
                (from?.let { " FROM $it" } ?: "") +
                (joins.takeIf { it.isNotEmpty() }?.joinToString(" ", prefix = " ") ?: "") +
                (where?.let { " WHERE $it" } ?: "") +
                (groupBy.takeIf { it.isNotEmpty() }?.let { " GROUP BY ${it.joinToString()}" } ?: "") +
                (orderBy.takeIf { it.isNotEmpty() }?.let { " ORDER BY ${it.joinToString()}" } ?: "") +
                "${ limit?.let { " LIMIT $it" } ?: "" }${ offset?.let { " OFFSET $it" } ?: "" };"

}