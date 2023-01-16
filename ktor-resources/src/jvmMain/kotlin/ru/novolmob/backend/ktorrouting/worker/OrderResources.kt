package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.*

@Serializable
@Resource("orders")
actual class Orders actual constructor(
    override val page: Long?,
    override val pageSize: Long?,
    override val sortByColumn: String?,
    override val sortOrder: String?
): Pagination {
    @Serializable
    @Resource("statuses")
    actual class Statuses actual constructor(
        override val page: Long?,
        override val pageSize: Long?,
        override val sortByColumn: String?,
        override val sortOrder: String?
    ): Pagination {
        @Serializable
        @Resource("details/{id}")
        actual class Detail actual constructor(actual val id: OrderStatusDetailId)
        @Serializable
        @Resource("{id}")
        actual class Id actual constructor(actual val id: OrderStatusId) {
            @Serializable
            @Resource("details")
            actual class Details actual constructor(actual val id: OrderStatusId)
        }
    }
    @Serializable
    @Resource("{id}")
    actual class Id actual constructor(actual val id: OrderId) {
        @Serializable
        @Resource("statuses")
        actual class Statuses actual constructor(actual val id: OrderId, actual val language: Language)
        @Serializable
        @Resource("devices")
        actual class Devices actual constructor(actual val id: OrderId)
    }
}