package ru.novolmob.backend.ktorrouting.user

import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.CityId


expect class Cities(
    page: Long? = null,
    pageSize: Long? = null,
    sortByColumn: String? = null,
    sortOrder: String? = null,
    language: Language
): Pagination {
    val language: Language
    class Id(id: CityId) {
        val id: CityId
    }
}