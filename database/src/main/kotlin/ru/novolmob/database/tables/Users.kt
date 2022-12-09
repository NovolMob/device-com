package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.birthday
import ru.novolmob.database.extensions.TableExtension.city
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.firstname
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.language
import ru.novolmob.database.extensions.TableExtension.lastname
import ru.novolmob.database.extensions.TableExtension.patronymic
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.database.models.ids.UserId

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