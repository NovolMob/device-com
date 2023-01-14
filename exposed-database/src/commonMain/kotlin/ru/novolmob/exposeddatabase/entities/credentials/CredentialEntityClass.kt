package ru.novolmob.exposeddatabase.entities.credentials

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.CredentialId
import ru.novolmob.exposeddatabase.tables.credentials.CredentialTable

open class CredentialEntityClass<EntityT: Entity<CredentialId>, Parent: Entity<ParentID>, ParentID: Comparable<ParentID>>(
    open val credentialTable: CredentialTable<ParentID>,
    entityType: Class<EntityT>? = null,
    entityCtor: ((EntityID<CredentialId>) -> EntityT)? = null
) : EntityClass<CredentialId, EntityT>(credentialTable, entityType, entityCtor) {

    fun findBy(parentID: ParentID): EntityT? = find { credentialTable.parent eq parentID }.singleOrNull()
}