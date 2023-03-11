package ru.novolmob.jdbcdatabase.procedures

import ru.novolmob.core.models.Amount
import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Baskets
import ru.novolmob.jdbcdatabase.tables.parameters.ProcedureParameter

object SetAmountInBasketProcedure: Procedure(procedureLanguage = DatabaseVocabulary.Language.PLPGSQL) {
    val userId = (idColumn("f_user_id", ::UserId) as ProcedureParameter).inMode()
    val deviceId = (idColumn("f_device_id", ::DeviceId) as ProcedureParameter).inMode()
    val newAmount = (amount("new_amount") as ProcedureParameter).inMode()
    val totalPrice = (price("total_price", 10, 2) as ProcedureParameter).outMode()

    override val body: String =
        "BEGIN\n" +
                "        IF ($newAmount > 0) THEN\n" +
                "            IF ((SELECT ${Baskets.amount} FROM $Baskets WHERE ${Baskets.userId} = $userId AND ${Baskets.deviceId} = $deviceId) IS NULL ) THEN\n" +
                "                raise notice 'INSERT';\n" +
                "                INSERT INTO $Baskets (${Baskets.userId.name}, ${Baskets.deviceId.name}, ${Baskets.amount.name}) VALUES ($userId, $deviceId, $newAmount);\n" +
                "            ELSE\n" +
                "                raise notice 'UPDATE';\n" +
                "                UPDATE $Baskets SET ${Baskets.amount.name} = $newAmount WHERE ${Baskets.userId} = $userId AND ${Baskets.deviceId} = $deviceId;\n" +
                "            END IF;\n" +
                "        ELSE\n" +
                "            DELETE FROM $Baskets WHERE ${Baskets.userId} = $userId AND ${Baskets.deviceId} = $deviceId;\n" +
                "        END IF;\n" +
                "        CALL ${TotalCostProcedure.titleString(userId.name, totalPrice.name)};\n" +
                "    END;"

    suspend fun call(
        userId: UserId,
        deviceId: DeviceId,
        newAmount: Amount
    ): Price = call(
        this.userId valueOf userId,
        this.deviceId valueOf deviceId,
        this.newAmount valueOf newAmount,
        this.totalPrice valueOf null
    ).first() as? Price ?: Price.ZERO

}