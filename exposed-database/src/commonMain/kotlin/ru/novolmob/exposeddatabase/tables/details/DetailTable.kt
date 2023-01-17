package ru.novolmob.exposeddatabase.tables.details

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.core.models.Language

abstract class DetailTable<ID: Comparable<ID>, ParentID: Comparable<ParentID>>: IdTable<ID>() {
    abstract val parent: Column<EntityID<ParentID>>
    abstract val language: Column<Language>
}