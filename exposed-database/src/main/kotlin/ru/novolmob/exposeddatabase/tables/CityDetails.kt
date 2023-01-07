package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationTime
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.language
import ru.novolmob.exposeddatabase.extensions.TableExtension.title
import ru.novolmob.exposeddatabase.extensions.TableExtension.updateTime

object CityDetails: IdTable<CityDetailId>() {
    override val id: Column<EntityID<CityDetailId>> = idColumn(constructor = ::CityDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    val title = title()
    val city = reference("city_id", Cities)
    val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}