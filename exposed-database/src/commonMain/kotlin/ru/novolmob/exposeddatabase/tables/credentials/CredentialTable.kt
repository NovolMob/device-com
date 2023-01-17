package ru.novolmob.exposeddatabase.tables.credentials

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.exposeddatabase.utils.TableUtil.email
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.password
import ru.novolmob.exposeddatabase.utils.TableUtil.phoneNumber
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime

abstract class CredentialTable<ParentID: Comparable<ParentID>>: IdTable<CredentialId>() {
    override val id: Column<EntityID<CredentialId>> = idColumn(constructor = ::CredentialId).entityId()
    override val primaryKey = PrimaryKey(id)

    val phoneNumber = phoneNumber().uniqueIndex()
    val email = email().uniqueIndex().nullable()
    val password = password()
    val updateDate = updateTime()
    abstract val parent: Column<EntityID<ParentID>>
}