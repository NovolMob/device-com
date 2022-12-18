package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.exposeddatabase.extensions.TableExtension.code
import ru.novolmob.exposeddatabase.extensions.TableExtension.creationDate
import ru.novolmob.exposeddatabase.extensions.TableExtension.idColumn
import ru.novolmob.core.models.ids.GrantedRightId

object GrantedRights: IdTable<GrantedRightId>() {
    override val id: Column<EntityID<GrantedRightId>> = idColumn(constructor = ::GrantedRightId).entityId()
    override val primaryKey = PrimaryKey(id)

    val worker = reference("worker_id", Workers)
    val code = code()
    val admin = reference("admin_id", Workers)
    val creationDate = creationDate()
}