package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.OrderId
import ru.novolmob.database.models.ids.UserId

@Serializable
@Resource("orders")
class Orders(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("{id}")
    class Id(val orders: Orders, val id: OrderId, val language: Language? = null) {
        @Serializable
        @Resource("statuses")
        class Statuses(val id: Id, val language: Language)
        @Serializable
        @Resource("devices")
        class Devices(val id: Id)
    }
    @Serializable
    @Resource("user_id/{userId}")
    class ByUserId(val orders: Orders, val userId: UserId)
}