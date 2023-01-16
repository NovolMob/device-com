package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.UserId

@Serializable
@Resource("users")
actual class Users actual constructor(
    override val page: Long?,
    override val pageSize: Long?,
    override val sortByColumn: String?,
    override val sortOrder: String?
): Pagination {
    @Serializable
    @Resource("{id}")
    actual class Id actual constructor(actual val id: UserId) {
        @Serializable
        @Resource("orders")
        actual class Orders actual constructor(actual val id: UserId)
    }
}