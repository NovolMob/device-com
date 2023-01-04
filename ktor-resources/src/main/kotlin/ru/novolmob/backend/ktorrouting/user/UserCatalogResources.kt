package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.CatalogSearchSample
import ru.novolmob.core.models.ids.DeviceTypeId

@Resource("catalog")
@Serializable
class Catalog(
    override val page: Int? = null,
    override val pageSize: Int? = null,
    override val searchString: String? = null,
    override val deviceTypeId: DeviceTypeId? = null,
): CatalogSearchSample