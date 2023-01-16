package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId

@Serializable
@Resource("points")
actual class Points actual constructor(
    override val page: Long?,
    override val pageSize: Long?,
    override val sortByColumn: String?,
    override val sortOrder: String?
) : Pagination {
    @Serializable
    @Resource("details/{id}")
    actual class Detail actual constructor(actual val id: PointDetailId)
    @Serializable
    @Resource("{id}")
    actual class Id actual constructor(actual val id: PointId) {
        @Serializable
        @Resource("details")
        actual class Details actual constructor(actual val id: PointId)
        @Serializable
        @Resource("workers")
        actual class Workers actual constructor(actual val id: PointId)
        @Serializable
        @Resource("orders")
        actual class Orders actual constructor(actual val id: PointId)
    }
}