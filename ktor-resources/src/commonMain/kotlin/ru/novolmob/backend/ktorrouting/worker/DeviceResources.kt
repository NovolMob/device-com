package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId

@Serializable
@Resource("devices")
class Devices(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("details/{id}")
    class Detail(val id: DeviceDetailId, val devices: Devices = Devices())
    @Serializable
    @Resource("{id}")
    class Id(val id: DeviceId, val devices: Devices = Devices()) {
        @Serializable
        @Resource("details")
        class Details(val id: Id)
        @Serializable
        @Resource("points")
        class Points(val id: Id)
    }
    @Serializable
    @Resource("types")
    class Types(
        override val page: Long? = null,
        override val pageSize: Long? = null,
        override val sortByColumn: String? = null,
        override val sortOrder: String? = null,
        val devices: Devices = Devices()
    ): Pagination {
        @Serializable
        @Resource("details/{id}")
        class Detail(val id: DeviceTypeDetailId, val types: Types = Types())
        @Serializable
        @Resource("{id}")
        class Id(val id: DeviceTypeId, val types: Types = Types()) {
            @Serializable
            @Resource("details")
            class Details(val id: Id)
        }
    }
}