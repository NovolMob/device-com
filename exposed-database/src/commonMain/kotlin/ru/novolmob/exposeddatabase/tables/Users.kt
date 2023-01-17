package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.core.models.ids.UserId
import ru.novolmob.exposeddatabase.utils.TableUtil.birthday
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.firstname
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.language
import ru.novolmob.exposeddatabase.utils.TableUtil.lastname
import ru.novolmob.exposeddatabase.utils.TableUtil.patronymic
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime

object Users: IdTable<UserId>() {
    override val id: Column<EntityID<UserId>> = idColumn(constructor = ::UserId).entityId()
    override val primaryKey = PrimaryKey(id)

    val firstname = firstname()
    val lastname = lastname()
    val patronymic = patronymic().nullable()
    val birthday = birthday().nullable()
    val city = reference("city_id", Cities).nullable()
    val language = language()
    val updateDate = updateTime()
    val creationDate = creationTime()
}