package ru.novolmob.backend.ktorrouting.worker

import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.*


expect class Orders(
    page: Long? = null,
    pageSize: Long? = null,
    sortByColumn: String? = null,
    sortOrder: String? = null
): Pagination {
    class Statuses(
        page: Long?,
        pageSize: Long?,
        sortByColumn: String?,
        sortOrder: String?
    ): Pagination {
        class Detail(id: OrderStatusDetailId) {
            val id: OrderStatusDetailId
        }
        class Id(id: OrderStatusId) {
            val id: OrderStatusId
            class Details(id: OrderStatusId) {
                val id: OrderStatusId
            }
        }
    }
    class Id(id: OrderId) {
        val id: OrderId
        class Statuses(id: OrderId, language: Language) {
            val id: OrderId
            val language: Language
        }
        class Devices(id: OrderId) {
            val id: OrderId
        }
    }
}