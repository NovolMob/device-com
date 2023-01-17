package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.*

@Serializable
@Resource("orders")
class Orders(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("statuses")
    class Statuses(
        override val page: Long? = null,
        override val pageSize: Long? = null,
        override val sortByColumn: String? = null,
        override val sortOrder: String? = null,
        val orders: Orders = Orders()
    ): Pagination {
        @Serializable
        @Resource("details/{id}")
        class Detail(val id: OrderStatusDetailId, val statuses: Statuses = Statuses())
        @Serializable
        @Resource("{id}")
        class Id(val id: OrderStatusId, val statuses: Statuses = Statuses()) {
            @Serializable
            @Resource("details")
            class Details(val id: Id)
        }
    }
    @Serializable
    @Resource("{id}")
    class Id(val id: OrderId, val orders: Orders = Orders()) {
        @Serializable
        @Resource("statuses")
        class Statuses(val language: Language, val id: Id)
        @Serializable
        @Resource("devices")
        class Devices(val id: Id)
    }
}