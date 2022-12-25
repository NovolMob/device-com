package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationTime
import ru.novolmob.exposeddatabase.extensions.TableExtension.firstname
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.language
import ru.novolmob.exposeddatabase.extensions.TableExtension.lastname
import ru.novolmob.exposeddatabase.extensions.TableExtension.patronymic
import ru.novolmob.exposeddatabase.extensions.TableExtension.updateTime
import ru.novolmob.core.models.ids.WorkerId

object Workers: IdTable<WorkerId>() {
    override val id: Column<EntityID<WorkerId>> = idColumn(constructor = ::WorkerId).entityId()
    override val primaryKey = PrimaryKey(id)

    val point = reference("point_id", Points).nullable()
    val firstname = firstname()
    val lastname = lastname()
    val patronymic = patronymic().nullable()
    val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}