package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.*
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.description
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.language
import ru.novolmob.jdbcdatabase.extensions.TableExtension.title
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object DeviceTypeDetails: DetailTable<DeviceTypeDetailId, DeviceTypeId>() {

    override val id = idColumn(constructor = ::DeviceTypeDetailId).primaryKey()
    override val parentId = reference("device_type_id", DeviceTypes.id).onDeleteCascade()
    val title = title()
    val description = description()
    override val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
        id: DeviceTypeDetailId? = null,
        deviceTypeId: DeviceTypeId,
        title: Title,
        description: Description,
        language: Language
    ) {
        val list = mutableListOf<ParameterValue<*>>(
            this.parentId valueOf deviceTypeId,
            this.title valueOf title,
            this.description valueOf description,
            this.language valueOf language,
        )
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: DeviceTypeDetailId,
        deviceTypeId: DeviceTypeId? = null,
        title: Title? = null,
        description: Description? = null,
        language: Language? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()

        deviceTypeId?.let { list.add(this.parentId valueOf it) }
        title?.let { list.add(this.title valueOf it) }
        description?.let { list.add(this.description valueOf it) }
        language?.let { list.add(this.language valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}