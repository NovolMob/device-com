package ru.novolmob.jdbcdatabase.procedures

import ru.novolmob.core.models.ids.OrderId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.*
import ru.novolmob.jdbcdatabase.tables.parameters.ProcedureParameter

object CancelOrderProcedure: Procedure(
    procedureLanguage = DatabaseVocabulary.Language.PLPGSQL
) {
    val orderId = (idColumn("f_order_id", ::OrderId) as ProcedureParameter)

    override val body: String =
        "DECLARE\n" +
                "        rec record;\n" +
                "    BEGIN\n" +
                "        SELECT ${OrderStatuses.active}, max(${OrderToStatusTable.creationTime}) FROM $OrderStatuses JOIN $OrderToStatusTable on ${OrderStatuses.id} = ${OrderToStatusTable.orderStatusId} WHERE ${OrderToStatusTable.orderId} = $orderId GROUP BY ${OrderStatuses.id} INTO rec;\n" +
                "        IF (rec.${OrderStatuses.active.name} = false) THEN\n" +
                "            raise exception 'Can''t cancel order';\n" +
                "        END IF;\n" +
                "        FOR rec IN SELECT * FROM $OrderToDeviceTable WHERE ${OrderToDeviceTable.orderId} = $orderId LOOP\n" +
                "            UPDATE $Devices SET ${Devices.amount.name} = ${Devices.amount.name} + rec.${OrderToDeviceTable.amount.name} WHERE ${Devices.id} = rec.${OrderToDeviceTable.deviceId.name};\n" +
                "        END LOOP;\n" +
                "        DELETE FROM $Orders WHERE ${Orders.id} = $orderId;\n" +
                "    END;"

    suspend fun call(
        orderId: OrderId
    ) {
        call(
            values = arrayOf(this.orderId valueOf orderId)
        )
    }
}