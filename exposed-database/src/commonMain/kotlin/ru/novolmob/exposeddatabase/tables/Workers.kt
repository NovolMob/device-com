package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.firstname
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.language
import ru.novolmob.exposeddatabase.utils.TableUtil.lastname
import ru.novolmob.exposeddatabase.utils.TableUtil.patronymic
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime
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