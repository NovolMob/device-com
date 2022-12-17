package ru.novolmob.backend.ktorrouting.worker

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.models.Pagination
import ru.novolmob.database.models.ids.BasketId
import ru.novolmob.database.models.ids.DeviceId
import ru.novolmob.database.models.ids.UserId

@Serializable
@Resource("/baskets")
class Baskets(
    override val page: Long? = null,
    override val pageSize: Long? = null,
    override val sortByColumn: String? = null,
    override val sortOrder: String? = null
) : Pagination {
    @Serializable
    @Resource("{id}")
    class Id(val baskets: Baskets, val id: BasketId)

    @Serializable
    @Resource("user_id/{userId}")
    class ByUserId(val baskets: Baskets = Baskets(), val userId: UserId) {
        @Serializable
        @Resource("devices")
        class Devices(val byUserId: ByUserId) {
            @Serializable
            @Resource("{deviceId}")
            class Id(val devices: Devices, val deviceId: DeviceId)
        }
    }
}