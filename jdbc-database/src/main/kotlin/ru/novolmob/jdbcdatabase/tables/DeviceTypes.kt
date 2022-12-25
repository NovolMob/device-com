package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue

object DeviceTypes: Table() {

    val id = idColumn(constructor = ::DeviceTypeId)
    val updateTime = updateTime()
    val creationTime = creationTime()

    fun insert(
        id: DeviceTypeId? = null
    ) {
        val list = mutableListOf<ColumnValue<*>>()
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

}