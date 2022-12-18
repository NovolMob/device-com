package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.birthday
import ru.novolmob.exposeddatabase.extensions.TableExtension.city
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationDate
import ru.novolmob.exposeddatabase.extensions.TableExtension.firstname
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.exposeddatabase.extensions.TableExtension.language
import ru.novolmob.exposeddatabase.extensions.TableExtension.lastname
import ru.novolmob.exposeddatabase.extensions.TableExtension.patronymic
import ru.novolmob.exposeddatabase.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.UserId

object Users: IdTable<UserId>() {
    override val id: Column<EntityID<UserId>> = idColumn(constructor = ::UserId).entityId()
    override val primaryKey = PrimaryKey(id)

    val firstname = firstname()
    val lastname = lastname()
    val patronymic = patronymic().nullable()
    val birthday = birthday().nullable()
    val city = city().nullable()
    val language = language()
    val updateDate = updateDate()
    val creationDate = creationDate()
}