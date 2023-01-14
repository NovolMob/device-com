package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.PointId

@Serializable
@Resource("points")
class Points(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("by_city")
    class ByCity(val points: Points)
    @Serializable
    @Resource("{id}")
    class Id(val points: Points, val id: PointId)
}