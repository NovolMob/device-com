package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.DdlCommandLogs
import java.sql.Types

object LoggingTrigger: Function(
    returnValue = "event_trigger" to Types.OTHER,
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    override suspend fun create() {
        super.create()
        kotlin.runCatching {
            database.statement("CREATE EVENT TRIGGER $name ON ddl_command_start EXECUTE PROCEDURE ${titleString()};".also { println(it) })
        }
    }

    suspend fun delete() {
        database.statement("DROP EVENT TRIGGER IF EXISTS $name;")
    }

    override fun body(): String =
        "BEGIN\n" +
                "    INSERT INTO $DdlCommandLogs VALUES (TG_TAG, user, inet_client_addr(), current_timestamp);\n" +
                "END;"
}