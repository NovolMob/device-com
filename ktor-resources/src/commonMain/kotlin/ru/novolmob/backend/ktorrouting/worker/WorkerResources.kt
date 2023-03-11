package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.WorkerId

@Serializable
@Resource("admin/workers")
class Workers(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("{id}")
    class Id(val id: WorkerId, val workers: Workers = Workers()) {
        @Serializable
        @Resource("rights")
        class Rights(val id: Id)
    }
}
@Serializable
@Resource("admin/login")
class Login
@Serializable
@Resource("admin/worker")
class Worker
@Serializable
@Resource("admin/logout")
class Logout
