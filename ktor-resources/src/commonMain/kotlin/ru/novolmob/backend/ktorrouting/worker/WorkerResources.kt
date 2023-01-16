package ru.novolmob.backend.ktorrouting.worker

import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.WorkerId

expect class Workers(
    page: Long? = null,
    pageSize: Long? = null,
    sortByColumn: String? = null,
    sortOrder: String? = null
): Pagination {
    class Worker
    class Logout
    class Id(id: WorkerId) {
        val id: WorkerId
        class Rights(id: WorkerId) {
            val id: WorkerId
        }
    }
    class Login
}