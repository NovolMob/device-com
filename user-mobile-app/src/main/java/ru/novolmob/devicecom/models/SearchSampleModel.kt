package ru.novolmob.devicecom.models

import ru.novolmob.backend.ktorrouting.user.Catalog
import ru.novolmob.backendapi.models.CatalogSearchSample
import ru.novolmob.core.models.ids.DeviceTypeId

data class SearchSampleModel(
    override val searchString: String = "",
    override val deviceTypeId: DeviceTypeId? = null,
    override val page: Int = 0,
    override val pageSize: Int = 20
): CatalogSearchSample {
    fun toCatalog(): Catalog =
        Catalog(page = page, pageSize = pageSize, searchString = searchString, deviceTypeId = deviceTypeId)
}