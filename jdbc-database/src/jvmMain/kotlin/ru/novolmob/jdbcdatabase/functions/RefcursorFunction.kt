package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.PreparedStatementExtension.use
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue
import java.sql.ResultSet
import java.sql.Types

abstract class RefcursorFunction(
    functionLanguage: DatabaseVocabulary.Language = DatabaseVocabulary.Language.SQL
): Function(
    returnValue = DatabaseVocabulary.REFCURSOR to Types.OTHER,
    functionLanguage = functionLanguage
) {
    suspend fun <T> callWithResultSet(
        vararg values: ParameterValue<*>,
        block: suspend ResultSet.() -> T
    ): T = callUpdate(*values) {
        (getObject(1) as ResultSet).use { block() }
    }
}