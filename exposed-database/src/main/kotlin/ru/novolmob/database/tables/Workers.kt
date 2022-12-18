package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.firstname
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.lastname
import ru.novolmob.database.extensions.TableExtension.patronymic
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.WorkerId

object Workers: IdTable<WorkerId>() {
    override val id: Column<EntityID<WorkerId>> = idColumn(constructor = ::WorkerId).entityId()
    override val primaryKey = PrimaryKey(id)

    val point = reference("point_id", Points).nullable()
    val firstname = firstname()
    val lastname = lastname()
    val patronymic = patronymic().nullable()
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}