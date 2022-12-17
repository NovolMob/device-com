package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.database.models.ids.PointId

@Serializable
@Resource("points")
class Points(
    override val page: Long?,
    override val pageSize: Long?,
    override val sortByColumn: String?,
    override val sortOrder: String?
) : Pagination {
    @Serializable
    @Resource("{id}")
    class Id(val points: Points, val id: PointId)
}