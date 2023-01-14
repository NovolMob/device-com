package ru.novolmob.jdbcdatabase.procedures

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.tables.parameters.ProcedureParameter
import ru.novolmob.jdbcdatabase.views.DetailView

object GetAmountOfPagesProcedure: Procedure(
    procedureLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val pageSize = (integer("page_size") as ProcedureParameter)
    val searchString = (text("search_string") as ProcedureParameter)
    val deviceTypeId = (idColumn("device_type_id", ::DeviceTypeId) as ProcedureParameter)
    val lang = (language("f_language") as ProcedureParameter)
    val amountOfPages = (integer("amount_of_pages") as ProcedureParameter).outMode()

    override val body: String =
        "DECLARE\n" +
                "        count int;\n" +
                "    BEGIN\n" +
                "        IF ($deviceTypeId IS NULL) THEN\n" +
                "            count = (SELECT count(${DetailView.DeviceDetailView.id}) FROM ${DetailView.DeviceDetailView} WHERE ${DetailView.DeviceDetailView.language} = $lang AND ${DetailView.DeviceDetailView.title} LIKE $searchString || '%');\n" +
                "        ELSE\n" +
                "            count = (SELECT count(${DetailView.DeviceDetailView.id}) FROM ${DetailView.DeviceDetailView} WHERE ${DetailView.DeviceDetailView.language} = $lang AND ${DetailView.DeviceDetailView.typeId} = $deviceTypeId AND ${DetailView.DeviceDetailView.title} LIKE $searchString || '%');\n" +
                "        END IF;\n" +
                "        $amountOfPages = (count / $pageSize) + (count % $pageSize > 0)::int;\n" +
                "    END;"

    suspend fun call(
        pageSize: Int,
        searchString: String,
        deviceTypeId: DeviceTypeId? = null,
        language: Language,
    ): Int = call(
        this.pageSize valueOf pageSize,
        this.searchString valueOf "\"$searchString",
        this.deviceTypeId valueOf deviceTypeId,
        this.lang valueOf language,
        this.amountOfPages valueOf null
    ).first() as Int

}