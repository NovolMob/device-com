package ru.novolmob.backend.ktorrouting.user

import io.ktor.resources.*
import kotlinx.serialization.Serializable
import ru.novolmob.core.models.ids.DeviceId

@Serializable
@Resource("basket")
class Basket {
    @Serializable
    @Resource("devices/{id}")
    class Device(val basket: Basket, val id: DeviceId)
}