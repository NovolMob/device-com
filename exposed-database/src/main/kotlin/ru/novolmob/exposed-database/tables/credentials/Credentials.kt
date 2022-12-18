package ru.novolmob.`exposed-database`.tables.credentials

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.`exposed-database`.extensions.TableExtension.email
import ru.novolmob.`exposed-database`.extensions.TableExtension.idColumn
import ru.novolmob.`exposed-database`.extensions.TableExtension.password
import ru.novolmob.`exposed-database`.extensions.TableExtension.phoneNumber
import ru.novolmob.`exposed-database`.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.CredentialId

open class Credentials: IdTable<CredentialId>() {
    override val id: Column<EntityID<CredentialId>> = idColumn(constructor = ::CredentialId).entityId()
    override val primaryKey = PrimaryKey(id)

    val phoneNumber = phoneNumber().uniqueIndex()
    val email = email().uniqueIndex().nullable()
    val password = password()
    val updateDate = updateDate()
}