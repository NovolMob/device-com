package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.exposeddatabase.utils.TableUtil.code
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.core.models.ids.GrantedRightId

object GrantedRights: IdTable<GrantedRightId>() {
    override val id: Column<EntityID<GrantedRightId>> = idColumn(constructor = ::GrantedRightId).entityId()
    override val primaryKey = PrimaryKey(id)

    val worker = reference("worker_id", Workers, onDelete = ReferenceOption.CASCADE)
    val code = code()
    val admin = reference("admin_id", Workers, onDelete = ReferenceOption.CASCADE)
    val creationDate = creationTime()
}