package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.procedures.TotalCostProcedure
import ru.novolmob.jdbcdatabase.tables.Baskets
import ru.novolmob.jdbcdatabase.tables.Devices
import ru.novolmob.jdbcdatabase.tables.OrderToDeviceTable
import ru.novolmob.jdbcdatabase.tables.Orders
import ru.novolmob.jdbcdatabase.views.BasketView
import ru.novolmob.jdbcdatabase.views.OrderView
import java.sql.ResultSet

object ConfirmOrderFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val userId = idColumn("f_user_id", ::UserId)
    val pointId = idColumn("f_point_id", ::PointId)
    val language = language("f_language")

    override fun body(): String =
        "DECLARE\n" +
                "        ref refcursor DEFAULT 'refcursor_$name';\n" +
                "        rec record;\n" +
                "        total_cost decimal(10, 2);\n" +
                "        order_id uuid;\n" +
                "    BEGIN\n" +
                "        FOR rec IN SELECT * FROM $BasketView WHERE ${BasketView.userId} = $userId LOOP\n" +
                "            if (rec.${BasketView.amountInBasket.name} > rec.${BasketView.amount.name}) THEN\n" +
                "                raise exception 'not enough devices';\n" +
                "            END IF;\n" +
                "        END LOOP;\n" +
                "        CALL ${TotalCostProcedure.titleString(userId.name, "total_cost")};\n" +
                "        INSERT INTO $Orders (${Orders.userId.name}, ${Orders.pointId.name}, ${Orders.totalCost.name}) VALUES ($userId, $pointId, total_cost) RETURNING ${Orders.id} INTO order_id;\n" +
                "        FOR rec IN SELECT * FROM $BasketView WHERE ${BasketView.userId} = $userId LOOP\n" +
                "            INSERT INTO $OrderToDeviceTable (${OrderToDeviceTable.orderId.name}, ${OrderToDeviceTable.deviceId.name}, ${OrderToDeviceTable.amount.name}, ${OrderToDeviceTable.priceForOne.name}) VALUES (order_id, rec.${BasketView.deviceId.name}, rec.${BasketView.amountInBasket.name}, rec.${BasketView.price.name});\n" +
                "            UPDATE $Devices SET ${Devices.amount.name} = ${Devices.amount.name} - rec.${BasketView.amountInBasket.name} WHERE ${Devices.id} = rec.${BasketView.deviceId.name};\n" +
                "        END LOOP;\n" +
                "        DELETE FROM $Baskets WHERE ${Baskets.userId} = $userId;\n" +
                "        OPEN ref FOR SELECT * FROM $OrderView WHERE ${OrderView.id} = order_id AND ${OrderView.cityLanguage} = $language AND ${OrderView.pointLanguage} = $language;\n" +
                "        RETURN ref;\n" +
                "    END;"

    suspend fun <T> call(
        userId: UserId,
        pointId: PointId,
        language: Language,
        block: suspend ResultSet.() -> T
    ): T =
        callWithResultSet(
            this.userId valueOf userId,
            this.pointId valueOf pointId,
            this.language valueOf language,
            block = block
        )

}