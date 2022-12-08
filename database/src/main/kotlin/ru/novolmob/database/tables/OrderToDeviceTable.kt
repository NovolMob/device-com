package ru.novolmob.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable

object OrderToDeviceTable: UUIDTable() {
    val order = reference("order_id", Orders)
    val device = reference("device_id", Devices)
    val amount = integer("amount")
    val priceForOne = decimal("price_for_one", 10, 2)
}