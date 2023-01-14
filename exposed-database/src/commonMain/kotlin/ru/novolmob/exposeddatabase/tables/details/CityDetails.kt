package ru.novolmob.exposeddatabase.tables.details

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.core.models.ids.CityDetailId
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.exposeddatabase.tables.Cities
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.language
import ru.novolmob.exposeddatabase.utils.TableUtil.title
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime

object CityDetails: DetailTable<CityDetailId, CityId>() {
    override val id: Column<EntityID<CityDetailId>> = idColumn(constructor = ::CityDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    val title = title()
    override val parent = reference("city_id", Cities, onDelete = ReferenceOption.CASCADE)
    override val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}