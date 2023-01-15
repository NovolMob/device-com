package ru.novolmob.jdbcdatabase.procedures

import ru.novolmob.core.models.Price
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.Baskets
import ru.novolmob.jdbcdatabase.tables.Devices
import ru.novolmob.jdbcdatabase.tables.parameters.ProcedureParameter

object TotalCostProcedure: Procedure(procedureLanguage = DatabaseVocabulary.Language.PLPGSQL) {
    val userId = (idColumn("f_user_id", ::UserId) as ProcedureParameter).inMode()
    val totalPrice = (price("total_price", 10, 2) as ProcedureParameter).outMode()

    override val body: String =
        "BEGIN\n" +
                "        $totalPrice = (SELECT sum(${Baskets.amount} * ${Devices.price}) " +
                "FROM $Baskets JOIN $Devices on ${Devices.id} = ${Baskets.deviceId} WHERE ${Baskets.userId} = $userId);\n" +
                "END;"

    suspend fun call(
        userId: UserId
    ): Price = call(
        this.userId valueOf userId,
        this.totalPrice valueOf null
    ).first() as? Price ?: Price.ZERO

}