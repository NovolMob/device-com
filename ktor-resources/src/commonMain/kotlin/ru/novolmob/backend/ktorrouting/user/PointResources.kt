package ru.novolmob.backend.ktorrouting.user

import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.PointId

expect class Points(
    page: Long? = null,
    pageSize: Long? = null,
    sortByColumn: String? = null,
    sortOrder: String? = null
): Pagination {
    class ByCity
    class Id(id: PointId) {
        val id: PointId
    }
}