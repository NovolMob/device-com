package ru.novolmob.exposeddatabase.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import ru.novolmob.exposeddatabase.utils.TableUtil.amount
import ru.novolmob.exposeddatabase.utils.TableUtil.creationTime
import ru.novolmob.exposeddatabase.utils.TableUtil.idColumn
import ru.novolmob.exposeddatabase.utils.TableUtil.updateTime
import ru.novolmob.core.models.ids.BasketId

object Baskets: IdTable<BasketId>() {
    override val id: Column<EntityID<BasketId>> = idColumn(constructor = ::BasketId).entityId()
    override val primaryKey = PrimaryKey(id)

    val user = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
    val device = reference("device_id", Devices, onDelete = ReferenceOption.CASCADE)
    val amount = amount()
    val updateTime = updateTime()
    val creationTime = creationTime()
}