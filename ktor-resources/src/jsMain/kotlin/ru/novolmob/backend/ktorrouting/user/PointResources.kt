package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.PointId

@Serializable
@Resource("points")
actual class Points actual constructor(
    override val page: Long?,
    override val pageSize: Long?,
    override val sortByColumn: String?,
    override val sortOrder: String?
): Pagination {
    @Serializable
    @Resource("by_city")
    actual class ByCity
    @Serializable
    @Resource("{id}")
    actual class Id actual constructor(actual val id: PointId)
}