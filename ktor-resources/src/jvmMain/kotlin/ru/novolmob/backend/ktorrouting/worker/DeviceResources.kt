package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId

@Serializable
@Resource("/devices")
actual class Devices actual constructor(
    override val page: Long?,
    override val pageSize: Long?,
    override val sortByColumn: String?,
    override val sortOrder: String?
): Pagination {
    @Serializable
    @Resource("details/{id}")
    actual class Detail actual constructor(actual val id: DeviceDetailId)
    @Serializable
    @Resource("{id}")
    actual class Id actual constructor(actual val id: DeviceId) {
        @Serializable
        @Resource("details")
        actual class Details actual constructor(actual val id: DeviceId)
        @Serializable
        @Resource("points")
        actual class Points actual constructor(actual val id: DeviceId)
    }
    @Serializable
    @Resource("types")
    actual class Types actual constructor(
        override val page: Long?,
        override val pageSize: Long?,
        override val sortByColumn: String?,
        override val sortOrder: String?,
    ): Pagination {
        @Serializable
        @Resource("details/{id}")
        actual class Detail actual constructor(actual val id: DeviceTypeDetailId)
        @Serializable
        @Resource("{id}")
        actual class Id actual constructor(actual val id: DeviceTypeId) {
            @Serializable
            @Resource("details")
            actual class Details actual constructor(actual val id: DeviceTypeId)
        }
    }
}