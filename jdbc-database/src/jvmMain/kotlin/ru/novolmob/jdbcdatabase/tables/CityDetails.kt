package ru.novolmob.jdbcdatabase.tables

import ru.novolmob.core.models.Language
import ru.novolmob.core.models.Title
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.jdbcdatabase.extensions.TableExtension.creationTime
import ru.novolmob.jdbcdatabase.extensions.TableExtension.idColumn
import ru.novolmob.jdbcdatabase.extensions.TableExtension.language
import ru.novolmob.jdbcdatabase.extensions.TableExtension.title
import ru.novolmob.jdbcdatabase.extensions.TableExtension.updateTime
import ru.novolmob.jdbcdatabase.tables.parameters.values.ParameterValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq

object CityDetails: IdTable<CityDetailId>() {

    override val id = idColumn(constructor = ::CityDetailId).primaryKey()
    val cityId = reference("city_id", Cities.id).onDeleteCascade()
    val title = title()
    val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    suspend fun insert(
        id: CityDetailId? = null,
        cityId: CityId,
        title: Title,
        language: Language
    ) {
        val list = mutableListOf(
            this.cityId valueOf cityId,
            this.title valueOf title,
            this.language valueOf language
        )
        id?.let { list.add(this.id valueOf it) }

        insert(values = list.toTypedArray())
    }

    suspend fun update(
        id: CityDetailId,
        cityId: CityId? = null,
        title: Title? = null,
        language: Language? = null
    ) {
        val list = mutableListOf<ParameterValue<*>>()
        cityId?.let { list.add(this.cityId valueOf it) }
        title?.let { list.add(this.title valueOf it) }
        language?.let { list.add(this.language valueOf it) }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }
}