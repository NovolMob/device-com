package ru.novolmob.jdbcdatabase.procedures

import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Baskets
import ru.novolmob.jdbcdatabase.tables.parameters.ProcedureParameter

object RemoveFromBasketProcedure: Procedure(procedureLanguage = DatabaseVocabulary.Language.PLPGSQL) {
    val userId = (idColumn("f_user_id", ::UserId) as ProcedureParameter).inMode()
    val deviceId = (idColumn("f_device_id", ::DeviceId) as ProcedureParameter).inMode()
    val totalPrice = (price("total_price", 10, 2) as ProcedureParameter).outMode()

    override val body: String =
        "BEGIN\n" +
                "        DELETE FROM $Baskets WHERE ${Baskets.userId} = $userId AND ${Baskets.deviceId} = $deviceId;\n" +
                "        CALL ${TotalCostProcedure.titleString(userId.name, totalPrice.name)};\n" +
                "    END;"

    suspend fun call(
        userId: UserId,
        deviceId: DeviceId
    ): Price = call(
        this.userId valueOf userId,
        this.deviceId valueOf deviceId,
        this.totalPrice valueOf null
    ).first() as? Price ?: Price.ZERO

}