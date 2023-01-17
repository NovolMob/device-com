package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.WorkerId

@Serializable
@Resource("workers")
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
    @Serializable
    @Resource("login")
    class Login(val workers: Workers = Workers())
    @Serializable
    @Resource("current")
    class Worker(val workers: Workers = Workers())
    @Serializable
    @Resource("logout")
    class Logout(val workers: Workers = Workers())
}