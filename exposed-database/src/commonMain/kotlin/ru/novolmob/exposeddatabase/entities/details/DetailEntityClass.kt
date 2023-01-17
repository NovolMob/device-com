package ru.novolmob.exposeddatabase.entities.details

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import ru.novolmob.core.models.Language
import ru.novolmob.exposeddatabase.tables.details.DetailTable

open class DetailEntityClass<ID: Comparable<ID>, EntityT: DetailEntity<ID, Parent, ParentID>, Parent: Entity<ParentID>, ParentID: Comparable<ParentID>>(
    val detailTable: DetailTable<ID, ParentID>,
    entityType: Class<EntityT>? = null,
    entityCtor: ((EntityID<ID>) -> EntityT)? = null
): EntityClass<ID, EntityT>(detailTable, entityType, entityCtor) {

    fun findBy(parentId: ParentID, language: Language): EntityT? =
        find { (detailTable.parent eq parentId) and (detailTable.language eq language) }.singleOrNull()

    fun allBy(parentId: ParentID): SizedIterable<EntityT> = find { detailTable.parent eq parentId }

    fun deleteAllBy(parentId: ParentID) = allBy(parentId).forEach(Entity<ID>::delete)

}