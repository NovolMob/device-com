package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.views.OrderToDeviceView
import java.sql.ResultSet

object GettingOrderDevicesFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val orderId = idColumn("f_order_id", constructor = ::OrderId)
    val language = language("f_language")

    override fun body(): String =
        "DECLARE\n" +
                "        ref refcursor DEFAULT 'refcursor_$name';\n" +
                "    BEGIN\n" +
                "        OPEN ref FOR SELECT * FROM $OrderToDeviceView " +
                "WHERE ${OrderToDeviceView.orderId} = $orderId AND ${OrderToDeviceView.deviceLanguage} = $language;\n" +
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