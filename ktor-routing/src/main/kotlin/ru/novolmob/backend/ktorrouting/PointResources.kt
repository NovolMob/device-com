package ru.novolmob.backend.ktorrouting

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.DeviceId
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
    class Id(val points: Points, val id: PointId, val language: Language? = null) {
        @Serializable
        @Resource("detail")
        class Detail(val id: Id, val language: Language)
        @Serializable
        @Resource("devices")
        class Devices(val id: Id) {
            @Serializable
            @Resource("{id}")
            class Id(val devices: Devices, val id: DeviceId)
        }
    }
}