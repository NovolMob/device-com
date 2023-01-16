package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.core.models.ids.DeviceId

@Serializable
@Resource("basket")
actual class Basket {
    @Serializable
    @Resource("devices/{id}")
    actual class Device actual constructor(
        actual val basket: Basket,
        actual val id: DeviceId
    )
}