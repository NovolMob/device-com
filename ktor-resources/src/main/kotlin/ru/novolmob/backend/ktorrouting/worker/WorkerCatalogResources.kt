package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.CatalogSearchSample
import ru.novolmob.core.models.Language
import ru.novolmob.core.models.ids.DeviceTypeId

@Resource("catalog")
@Serializable
class Catalog(
    override val page: Int?,
    override val pageSize: Int?,
    override val searchString: String?,
    override val deviceTypeId: DeviceTypeId?,
    val language: Language
): CatalogSearchSample