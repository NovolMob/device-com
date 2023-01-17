package ru.novolmob.jdbcdatabase.functions

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.views.DetailView
import java.sql.ResultSet

object GettingCatalogFunction: RefcursorFunction(
    functionLanguage = DatabaseVocabulary.Language.PLPGSQL
) {

    val page = integer("page")
    val pageSize = integer("page_size")
    val searchString = text("search_string")
    val deviceTypeId = idColumn("device_type_id", ::DeviceTypeId)
    val lang = language("f_language")

    override fun body(): String =
        "DECLARE\n" +
                "        ref refcursor DEFAULT 'cursor_$name';\n" +
                "    BEGIN\n" +
                "        IF ($deviceTypeId IS NULL) THEN\n" +
                "            OPEN ref FOR SELECT * FROM ${DetailView.DeviceDetailView} WHERE ${DetailView.DeviceDetailView.language} = $lang AND ${DetailView.DeviceDetailView.title} LIKE $searchString || '%' ORDER BY ${DetailView.DeviceDetailView.title} LIMIT $pageSize OFFSET $pageSize * $page;\n" +
                "        ELSE\n" +
                "            OPEN ref FOR SELECT * FROM ${DetailView.DeviceDetailView} WHERE ${DetailView.DeviceDetailView.language} = $lang AND ${DetailView.DeviceDetailView.typeId} = $deviceTypeId AND ${DetailView.DeviceDetailView.title} LIKE $searchString || '%' ORDER BY ${DetailView.DeviceDetailView.title} LIMIT $pageSize OFFSET $pageSize * $page;\n" +
                "        END IF;\n" +
                "        RETURN ref;\n" +
                "    END;"

    suspend fun <T> call(
        page: Int,
        pageSize: Int,
        searchString: String,
        deviceTypeId: DeviceTypeId? = null,
        language: Language,
        block: suspend ResultSet.() -> T
    ): T =
        callWithResultSet(
            this.page valueOf page,
            this.pageSize valueOf pageSize,
            this.searchString valueOf "\"$searchString",
            this.deviceTypeId valueOf deviceTypeId,
            this.lang valueOf language,
            block = block
        )

}