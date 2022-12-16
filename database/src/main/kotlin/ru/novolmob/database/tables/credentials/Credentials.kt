package ru.novolmob.database.tables.credentials

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.email
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.password
import ru.novolmob.database.extensions.TableExtension.phoneNumber
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.database.models.ids.CredentialId

open class Credentials: IdTable<CredentialId>() {
    override val id: Column<EntityID<CredentialId>> = idColumn(constructor = ::CredentialId).entityId()
    override val primaryKey = PrimaryKey(id)

    val phoneNumber = phoneNumber().uniqueIndex()
    val email = email().uniqueIndex().nullable()
    val password = password()
    val updateDate = updateDate()
}