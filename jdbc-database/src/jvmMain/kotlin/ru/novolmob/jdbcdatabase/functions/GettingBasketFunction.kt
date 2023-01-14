package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.views.BasketView
import java.sql.ResultSet

object GettingBasketFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val userId = idColumn("f_user_id", ::UserId)
    val lang = language("f_language")

    override fun body(): String =
        "DECLARE\n" +
                "        ref refcursor DEFAULT 'cursor_$name';\n" +
                "    BEGIN\n" +
                "        OPEN ref FOR SELECT * FROM $BasketView " +
                "WHERE ${BasketView.userId} = $userId AND ${BasketView.language} = $lang ORDER BY ${BasketView.creationTime} DESC;\n" +
                "        RETURN ref;\n" +
                "    END;"

    suspend fun <T> call(
        userId: UserId,
        language: Language,
        block: suspend ResultSet.() -> T
    ): T =
        callWithResultSet(
            this.userId valueOf userId,
            this.lang valueOf language,
            block = block
        )

}