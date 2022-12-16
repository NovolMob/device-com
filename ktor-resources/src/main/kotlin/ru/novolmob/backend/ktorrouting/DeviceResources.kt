package ru.novolmob.backend.ktorrouting

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.database.models.Language
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.DeviceTypeId

@Serializable
@Resource("/devices")
class Devices(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
): Pagination {
    @Serializable
    @Resource("{id}")
    class Id(val devices: Devices, val id: DeviceId, val language: Language? = null) {
        @Serializable
        @Resource("detail")
        class Detail(val id: Id, val language: Language)
        @Serializable
        @Resource("points")
        class Points(val id: Id, val language: Language)
    }
    @Serializable
    @Resource("types")
    class Types(
        override val page: Long? = null,
        override val pageSize: Long? = null,
        override val sortByColumn: String? = null,
        override val sortOrder: String? = null,
        val devices: Devices
    ): Pagination {
        @Serializable
        @Resource("{id}")
        class Id(val types: Types, val id: DeviceTypeId, val language: Language? = null) {
            @Serializable
            @Resource("detail")
            class Detail(val id: Id, val language: Language)
        }
    }
}