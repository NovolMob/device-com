package ru.novolmob.backend.ktorrouting.general

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.core.models.ids.DeviceTypeId

@Resource("catalog")
@Serializable
class Catalog(
    val page: Int?,
    val pageSize: Int?,
    val searchString: String?,
    val deviceTypeId: DeviceTypeId?
)