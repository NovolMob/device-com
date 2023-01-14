package ru.novolmob.jdbcdatabase.tables

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
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq

object DeviceDetails: IdTable<DeviceDetailId>() {

    override val id = idColumn(constructor = ::DeviceDetailId).primaryKey()
    val deviceId = reference("device_id", Devices.id).onDeleteCascade()
    val title = title()
    val description = description()
    val features = features()
    val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
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

        id?.let { list.add(this.id valueOf id) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: DeviceDetailId,
        deviceId: DeviceId? = null,
        title: Title? = null,
        description: Description? = null,
        features: Features? = null,
        language: Language? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()

        deviceId?.let { list.add(this.deviceId valueOf deviceId) }
        title?.let { list.add(this.title valueOf title) }
        description?.let { list.add(this.description valueOf description) }
        features?.let { list.add(this.features valueOf features) }
        language?.let { list.add(this.language valueOf language) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}