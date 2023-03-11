package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.UserId

@Serializable
@Resource("admin/users")
class Users(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("{id}")
    class Id(val id: UserId, val users: Users = Users()) {
        @Serializable
        @Resource("orders")
        class Orders(val id: Id)
    }
}