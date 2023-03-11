package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Table
import java.sql.Types

class UpdateTimeFunction(
    val tables: List<Table>,
): Function(
    returnValue = "trigger" to Types.OTHER,
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    override suspend fun create() {
        super.create()
        tables.forEach {
            database.statement(
                "CREATE OR REPLACE TRIGGER ${name}_trigger BEFORE UPDATE ON ${it.name} FOR EACH ROW EXECUTE PROCEDURE ${titleString()};".also { println(it) }
            )
        }
    }

    override fun body(): String =
        "BEGIN\n" +
                "        new.update_time = current_timestamp;\n" +
                "        return new;\n" +
                "    END;"
}