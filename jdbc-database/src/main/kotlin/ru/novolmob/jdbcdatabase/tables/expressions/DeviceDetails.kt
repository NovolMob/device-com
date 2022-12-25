package ru.novolmob.jdbcdatabase.tables.expressions

import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Features
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.description
import ru.novolmob.jdbcdatabase.extensions.TableExtension.features
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.language
import ru.novolmob.jdbcdatabase.extensions.TableExtension.title
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.Devices
import ru.novolmob.jdbcdatabase.tables.Table
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq

object DeviceDetails: Table() {

    val id = idColumn(constructor = ::DeviceDetailId)
    val deviceId = reference("device_id", Devices.id)
    val title = title()
    val description = description()
    val features = features()
    val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    fun insert(
        id: DeviceDetailId? = null,
        deviceId: DeviceId,
        title: Title,
        description: Description,
        features: Features,
        language: Language
    ) {
        val list = mutableListOf(
            this.deviceId valueOf deviceId,
            this.title valueOf title,
            this.description valueOf description,
            this.features valueOf features,
            this.language valueOf language
        )

        id?.let { this.id valueOf id }

        insert(values = list.toTypedArray())
    }

    fun update(
        id: DeviceDetailId,
        deviceId: DeviceId? = null,
        title: Title? = null,
        description: Description? = null,
        features: Features? = null,
        language: Language? = null
    ) {
        val list = mutableListOf<ColumnValue<*>>()

        deviceId?.let { this.deviceId valueOf deviceId }
        title?.let { this.title valueOf title }
        description?.let { this.description valueOf description }
        features?.let { this.features valueOf features }
        language?.let { this.language valueOf language }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}