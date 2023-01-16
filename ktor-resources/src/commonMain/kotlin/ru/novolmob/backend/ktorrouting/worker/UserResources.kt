package ru.novolmob.backend.ktorrouting.worker

import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.UserId


expect class Users(
    page: Long? = null,
    pageSize: Long? = null,
    sortByColumn: String? = null,
    sortOrder: String? = null
): Pagination {
    class Id(id: UserId) {
        val id: UserId
        class Orders(id: UserId) {
            val id: UserId
        }
    }
}