package ru.novolmob.exposeddatabase.entities.credentials

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.ids.CredentialId

abstract class CredentialEntity<Parent: Entity<ParentID>, ParentID: Comparable<ParentID>>(id: EntityID<CredentialId>) : Entity<CredentialId>(id) {
    abstract var parent: Parent
}