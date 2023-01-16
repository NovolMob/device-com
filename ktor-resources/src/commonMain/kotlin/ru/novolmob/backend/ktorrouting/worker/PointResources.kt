package ru.novolmob.backend.ktorrouting.worker

import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId


expect class Points(
    page: Long?,
    pageSize: Long?,
    sortByColumn: String?,
    sortOrder: String?
) : Pagination {
    class Detail(id: PointDetailId) {
        val id: PointDetailId
    }
    class Id(id: PointId) {
        val id: PointId
        class Details(id: PointId) {
            val id: PointId
        }
        class Workers(id: PointId) {
            val id: PointId
        }
        class Orders(id: PointId) {
            val id: PointId
        }
    }
}