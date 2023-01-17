package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId

@Serializable
@Resource("devices")
class Devices {
    @Serializable
    @Resource("{id}")
    class Id(val id: DeviceId, val devices: Devices = Devices())
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
        @Resource("{id}")
        class Id(val id: DeviceTypeId, val types: Types = Types())
    }
}