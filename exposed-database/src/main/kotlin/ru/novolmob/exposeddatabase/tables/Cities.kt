package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationTime
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn

object Cities: IdTable<CityId>() {
    override val id: Column<EntityID<CityId>> = idColumn(constructor = ::CityId).entityId()
    override val primaryKey = PrimaryKey(id)

    val creationDate = creationTime()
}