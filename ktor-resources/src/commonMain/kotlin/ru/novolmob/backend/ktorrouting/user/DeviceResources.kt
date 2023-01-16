package ru.novolmob.backend.ktorrouting.user

import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeId


expect class Devices {
    class Id(id: DeviceId) {
        val id: DeviceId
    }
    class Types(
        page: Long? = null,
        pageSize: Long? = null,
        sortByColumn: String? = null,
        sortOrder: String? = null,
    ): Pagination {
        class Id(id: DeviceTypeId) {
            val id: DeviceTypeId
        }
    }
}