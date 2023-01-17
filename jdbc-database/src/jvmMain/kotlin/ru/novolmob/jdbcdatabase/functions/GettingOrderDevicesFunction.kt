package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.OrderToDeviceTable
import ru.novolmob.jdbcdatabase.views.DetailView
import ru.novolmob.jdbcdatabase.views.OrderToStatusView
import java.sql.ResultSet

object GettingOrderDevicesFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val orderId = idColumn("f_order_id", constructor = ::OrderId)
    val language = language("f_language")

    override fun body(): String =
        "DECLARE\n" +
                "        ref refcursor DEFAULT 'refcursor_$OrderToStatusView';\n" +
                "    BEGIN\n" +
                "        OPEN ref FOR SELECT * FROM ${DetailView.DeviceDetailView} " +
                "JOIN $OrderToDeviceTable ON ${DetailView.DeviceDetailView.id} = ${OrderToDeviceTable.deviceId} " +
                "WHERE ${OrderToDeviceTable.orderId} = $orderId AND ${DetailView.DeviceDetailView.language} = $language;\n" +
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