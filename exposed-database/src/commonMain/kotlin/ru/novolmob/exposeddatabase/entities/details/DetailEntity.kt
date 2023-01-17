package ru.novolmob.exposeddatabase.entities.details

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import ru.novolmob.core.models.Language

abstract class DetailEntity<ID: Comparable<ID>, Parent: Entity<ParentID>, ParentID: Comparable<ParentID>>(id: EntityID<ID>) : Entity<ID>(id) {

    abstract var parent: Parent
    abstract var language: Language

}