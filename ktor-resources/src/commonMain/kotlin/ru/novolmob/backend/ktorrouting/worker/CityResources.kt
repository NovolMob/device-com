package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.CityId
import ru.novolmob.core.models.ids.PointDetailId

@Serializable
@Resource("cities")
class Cities(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("details/{id}")
    class Details(val id: PointDetailId, val cities: Cities = Cities())
    @Serializable
    @Resource("{id}")
    class Id(val id: CityId, val cities: Cities = Cities()) {
        @Serializable
        @Resource("details")
        class Details(val id: Id)
        @Serializable
        @Resource("users")
        class Users(val id: Id)
    }
}