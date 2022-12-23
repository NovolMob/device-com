package ru.novolmob.backendapi.models

import ru.novolmob.core.models.ids.DeviceTypeId

interface CatalogSearchSample {
    val page: Int?
    val pageSize: Int?
    val searchString: String?
    val deviceTypeId: DeviceTypeId?
}