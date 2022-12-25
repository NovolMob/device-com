package ru.novolmob.jdbcdatabase.views

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcdatabase.databases.DatabaseVocabulary
import ru.novolmob.jdbcdatabase.extensions.IColumnExtension.tableColumnString
import ru.novolmob.jdbcdatabase.tables.DeviceTypeDetails
import ru.novolmob.jdbcdatabase.tables.DeviceTypes
import ru.novolmob.jdbcdatabase.tables.Devices
import ru.novolmob.jdbcdatabase.tables.columns.IColumn
import ru.novolmob.jdbcdatabase.tables.expressions.DeviceDetails
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import java.sql.ResultSet

sealed class DetailView<T: Any>(
    name: String? = null,
    private val columns: List<IColumn<*>>,
    private val first: IColumn<T>,
    private val second: IColumn<T>,
    private val languageColumn: IColumn<Language>
): View(name) {

    private fun fromTable() = first.table
    private fun joinString() = DatabaseVocabulary.join(second.table.tableName, first.tableColumnString(), second.tableColumnString())

    override fun create() {
        val sql = DatabaseVocabulary.createViewSql(
            name,
            DatabaseVocabulary.selectSql(
                fromTable().tableName,
                columns = columns.map { it.tableColumnString() },
                joins = listOf(joinString())
            )
        ).also { println("View $name created:  $it") }
        database.statement(sql)
    }

    fun select(id: T): ResultSet = select(expression = first eq id)
    fun select(id: T, language: Language): ResultSet = select(expression = (first eq id) and (languageColumn eq language))


    object DeviceTypeDetailView: DetailView<DeviceTypeId>(
        columns = listOf(
            DeviceTypes.id,
            DeviceTypeDetails.title,
            DeviceTypeDetails.description,
            DeviceTypeDetails.language,
        ),
        first = DeviceTypes.id,
        second = DeviceTypeDetails.deviceTypeId,
        languageColumn = DeviceTypeDetails.language
    )

    object DeviceDetailView: DetailView<DeviceId>(
        columns = listOf(
            Devices.id,
            DeviceDetails.title,
            DeviceDetails.description,
            DeviceDetails.features,
            DeviceDetails.language,
        ),
        first = Devices.id,
        second = DeviceDetails.deviceId,
        languageColumn = DeviceDetails.language
    )

}