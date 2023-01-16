package ru.novolmob.backend.ktorrouting.user

import ru.novolmob.backendapi.models.CatalogSearchSample
import ru.novolmob.core.models.ids.DeviceTypeId


expect class Catalog(
    page: Int? = null,
    pageSize: Int? = null,
    searchString: String? = null,
    deviceTypeId: DeviceTypeId? = null,
): CatalogSearchSample