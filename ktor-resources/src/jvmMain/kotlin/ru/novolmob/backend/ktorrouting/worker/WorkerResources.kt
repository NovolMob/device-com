package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.WorkerId

@Serializable
@Resource("worker")
actual class Workers actual constructor(
    override val page: Long?,
    override val pageSize: Long?,
    override val sortByColumn: String?,
    override val sortOrder: String?
): Pagination {
    @Serializable
    @Resource("{id}")
    actual class Id actual constructor(actual val id: WorkerId) {
        @Serializable
        @Resource("rights")
        actual class Rights actual constructor(actual val id: WorkerId)
    }
    @Serializable
    @Resource("login")
    actual class Login
    @Serializable
    @Resource("current")
    actual class Worker
    @Serializable
    @Resource("logout")
    actual class Logout
}