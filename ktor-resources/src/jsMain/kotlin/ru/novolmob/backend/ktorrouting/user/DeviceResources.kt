package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId

@Serializable
@Resource("/devices")
actual class Devices {
    @Serializable
    @Resource("{id}")
    actual class Id actual constructor(actual val id: DeviceId)
    @Serializable
    @Resource("types")
    actual class Types actual constructor(
        override val page: Long?,
        override val pageSize: Long?,
        override val sortByColumn: String?,
        override val sortOrder: String?
    ): Pagination {
        @Serializable
        @Resource("{id}")
        actual class Id actual constructor(actual val id: DeviceTypeId)
    }
}