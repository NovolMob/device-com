package ru.novolmob.backend.ktorrouting.worker

import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.core.models.ids.DeviceTypeDetailId
import ru.novolmob.core.models.ids.DeviceTypeId


expect class Devices(
    page: Long? = null,
    pageSize: Long? = null,
    sortByColumn: String? = null,
    sortOrder: String? = null
): Pagination {
    class Detail(id: DeviceDetailId) {
        val id: DeviceDetailId
    }
    class Id(id: DeviceId) {
        val id: DeviceId
        class Details(id: DeviceId) {
            val id: DeviceId
        }
        class Points(id: DeviceId) {
            val id: DeviceId
        }
    }

    class Types(
        page: Long? = null,
        pageSize: Long? = null,
        sortByColumn: String? = null,
        sortOrder: String? = null
    ): Pagination {
        class Detail(id: DeviceTypeDetailId) {
            val id: DeviceTypeDetailId
        }
        class Id(id: DeviceTypeId) {
            val id: DeviceTypeId
            class Details(id: DeviceTypeId) {
                val id: DeviceTypeId
            }
        }
    }
}