package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.PointDetailId
import ru.novolmob.core.models.ids.PointId

@Serializable
@Resource("points")
class Points(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
) : Pagination {
    @Serializable
    @Resource("details/{id}")
    class Detail(val id: PointDetailId, val points: Points = Points())
    @Serializable
    @Resource("{id}")
    class Id(val id: PointId, val points: Points = Points()) {
        @Serializable
        @Resource("details")
        class Details(val id: Id)
        @Serializable
        @Resource("workers")
        class Workers(val id: Id)
        @Serializable
        @Resource("orders")
        class Orders(val id: Id)
    }
}