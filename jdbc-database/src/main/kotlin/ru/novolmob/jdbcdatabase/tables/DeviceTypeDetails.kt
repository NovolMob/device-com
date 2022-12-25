package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.description
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.language
import ru.novolmob.jdbcdatabase.extensions.TableExtension.title
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue

object DeviceTypeDetails: Table() {

    val id = idColumn(constructor = ::DeviceTypeDetailId)
    val deviceTypeId = reference("device_type_id", DeviceTypes.id)
    val title = title()
    val description = description()
    val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    fun insert(
        id: DeviceTypeDetailId? = null,
        deviceTypeId: DeviceTypeId,
        title: Title,
        description: Description,
        language: Language
    ) {
        val list = mutableListOf<ColumnValue<*>>(
            this.deviceTypeId valueOf deviceTypeId,
            this.title valueOf title,
            this.description valueOf description,
            this.language valueOf language,
        )
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

}