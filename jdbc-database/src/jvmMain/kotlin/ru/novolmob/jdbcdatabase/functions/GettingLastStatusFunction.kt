package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.ParameterExtension.fullName
import ru.novolmob.jdbcdatabase.views.OrderToStatusView
import java.sql.ResultSet

object GettingLastStatusFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val orderId = idColumn("f_order_id", constructor = ::OrderId)
    val language = language("f_language")

    override fun body(): String =
        "DECLARE\n" +
                "        ref refcursor DEFAULT 'refcursor_$OrderToStatusView';\n" +
                "    BEGIN\n" +
                "        OPEN ref FOR SELECT DISTINCT ON (${OrderToStatusView.orderId}) *, max(${OrderToStatusView.creationTime}) " +
                "FROM $OrderToStatusView " +
                "WHERE ${OrderToStatusView.orderId} = $orderId AND ${OrderToStatusView.orderStatusLanguage} = $language " +
                "GROUP BY ${OrderToStatusView.parameters.joinToString { it.fullName() }};\n" +
                "        RETURN ref;\n" +
                "    END;"

    suspend fun <T> call(
        orderId: OrderId,
        language: Language,
        block: suspend ResultSet.() -> T
    ): T =
        callWithResultSet(
            this.orderId valueOf orderId,
            this.language valueOf language,
            block = block
        )

}