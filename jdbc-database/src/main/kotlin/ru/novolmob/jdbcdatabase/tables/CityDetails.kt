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
import ru.novolmob.jdbcdatabase.tables.columns.values.ColumnValue
import ru.novolmob.jdbcdatabase.tables.expressions.Expression.Companion.eq

object CityDetails: Table() {

    val id = idColumn(constructor = ::CityDetailId)
    val cityId = reference("city_id", Cities.id)
    val title = title()
    val language = language()
    val updateTime = updateTime()
    val creationTime = creationTime()

    fun insert(
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
        id?.let { this.id valueOf it }

        insert(values = list.toTypedArray())
    }

    fun update(
        id: CityDetailId,
        cityId: CityId? = null,
        title: Title? = null,
        language: Language? = null
    ) {
        val list = mutableListOf<ColumnValue<*>>()
        cityId?.let { this.cityId valueOf it }
        title?.let { this.title valueOf it }
        language?.let { this.language valueOf it }

        update(
            newValues = list.toTypedArray(),
            expression = this.id eq id
        )
    }
}