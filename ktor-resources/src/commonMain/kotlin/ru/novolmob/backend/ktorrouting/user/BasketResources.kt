package ru.novolmob.backend.ktorrouting.user

import ru.novolmob.core.models.ids.DeviceId

expect class Basket {
    class Device(basket: Basket, id: DeviceId) {
        val basket: Basket
        val id: DeviceId
    }
}