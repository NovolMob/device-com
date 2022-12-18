package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import ru.novolmob.database.extensions.TableExtension.amount
import ru.novolmob.database.extensions.TableExtension.creationDate
import ru.novolmob.database.extensions.TableExtension.idColumn
import ru.novolmob.database.extensions.TableExtension.updateDate
import ru.novolmob.core.models.ids.BasketId

object Baskets: IdTable<BasketId>() {
    override val id: Column<EntityID<BasketId>> = idColumn(constructor = ::BasketId).entityId()
    override val primaryKey = PrimaryKey(id)

    val user = reference("user_id", Users)
    val device = reference("device_id", Devices)
    val amount = amount()
    val updateDate = updateDate()
    val creationDate = creationDate()
}