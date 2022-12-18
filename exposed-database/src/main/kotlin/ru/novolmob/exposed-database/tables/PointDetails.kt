package ru.novolmob.`exposed-database`.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.`exposed-database`.extensions.TableExtension.address
import ru.novolmob.`exposed-database`.extensions.TableExtension.creationDate
import ru.novolmob.`exposed-database`.extensions.TableExtension.description
import ru.novolmob.`exposed-database`.extensions.TableExtension.idColumn
import ru.novolmob.`exposed-database`.extensions.TableExtension.language
import ru.novolmob.`exposed-database`.extensions.TableExtension.schedule
import ru.novolmob.`exposed-database`.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.PointDetailId

object PointDetails: IdTable<PointDetailId>() {
    override val id: Column<EntityID<PointDetailId>> = idColumn(constructor = ::PointDetailId).entityId()
    override val primaryKey = PrimaryKey(id)

    val point = reference("point_id", Points)
    val address = address()
    val schedule = schedule()
    val description = description()
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}