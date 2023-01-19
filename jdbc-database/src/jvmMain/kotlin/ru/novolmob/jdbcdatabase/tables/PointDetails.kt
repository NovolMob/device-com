package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Address
import ru.novolmob.core.models.Description
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Schedule
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.address
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.description
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.language
import ru.novolmob.jdbcdatabase.extensions.TableExtension.schedule
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object PointDetails: DetailTable<PointDetailId, PointId>() {

    override val id = idColumn(constructor = ::PointDetailId).primaryKey()
    override val parentId = reference("point_id", Points.id).onDeleteCascade()
    val address = address()
    val schedule = schedule()
    val description = description()
    override val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
        id: PointDetailId? = null,
        pointId: PointId,
        address: Address,
        schedule: Schedule,
        description: Description,
        language: Language
    ) {
        val list = mutableListOf<ParameterValue<*>>(
            this.parentId valueOf pointId,
            this.address valueOf address,
            this.schedule valueOf schedule,
            this.description valueOf description,
            this.language valueOf language,
        )
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: PointDetailId,
        pointId: PointId? = null,
        address: Address? = null,
        schedule: Schedule? = null,
        description: Description? = null,
        language: Language? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()

        pointId?.let { list.add(this.parentId valueOf it) }
        address?.let { list.add(this.address valueOf it) }
        schedule?.let { list.add(this.schedule valueOf it) }
        description?.let { list.add(this.description valueOf it) }
        language?.let { list.add(this.language valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}