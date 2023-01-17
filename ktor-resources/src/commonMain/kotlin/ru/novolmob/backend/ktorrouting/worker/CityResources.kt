package ru.novolmob.backend.ktorrouting.worker

import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.CityId


expect class Cities(
    page: Long? = null,
    pageSize: Long? = null,
    sortByColumn: String? = null,
    sortOrder: String? = null
): Pagination {
    class Id(id: CityId) {
        val id: CityId
        class Users(id: CityId) {
            val id: CityId
        }
    }
}