package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.PointId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue

object Points: IdTable<PointId>()  {

    override val id = idColumn(constructor = ::PointId).primaryKey()
    val cityId = reference("city_id", Cities.id).onDeleteCascade()
    val updateTime = updateTime()
    val creationTime = creationTime()


    suspend fun insert(
        id: PointId? = null,
        cityId: CityId
    ) {
        val list = mutableListOf<ParameterValue<*>>(
            this.cityId valueOf cityId
        )
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: PointId,
        cityId: CityId? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        cityId?.let { list.add(this.cityId valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }

}