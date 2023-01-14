package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Firstname
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Lastname
import ru.novolmob.core.models.Patronymic
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.core.models.ids.WorkerId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.firstname
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.language
import ru.novolmob.jdbcdatabase.extensions.TableExtension.lastname
import ru.novolmob.jdbcdatabase.extensions.TableExtension.patronymic
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object Workers: IdTable<WorkerId>() {

    override val id = idColumn(constructor = ::WorkerId).primaryKey()
    val pointId = reference("point_id", Points.id).nullable()
    val firstname = firstname()
    val lastname = lastname()
    val patronymic = patronymic().nullable()
    val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
        id: WorkerId? = null,
        pointId: PointId? = null,
        firstname: Firstname,
        lastname: Lastname,
        patronymic: Patronymic? = null,
        language: Language
    ) {
        val list = mutableListOf(
            this.firstname valueOf firstname,
            this.lastname valueOf lastname,
            this.language valueOf language
        )
        id?.let { list.add(this.id valueOf it) }
        pointId?.let { list.add(this.pointId valueOf pointId) }
        patronymic?.let { list.add(this.patronymic valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: WorkerId,
        pointId: PointId? = null,
        firstname: Firstname? = null,
        lastname: Lastname? = null,
        patronymic: Patronymic? = null,
        language: Language? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        pointId?.let { list.add(this.pointId valueOf pointId) }
        firstname?.let { list.add(this.firstname valueOf it) }
        lastname?.let { list.add(this.lastname valueOf it) }
        patronymic?.let { list.add(this.patronymic valueOf it) }
        language?.let { list.add(this.language valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }
}