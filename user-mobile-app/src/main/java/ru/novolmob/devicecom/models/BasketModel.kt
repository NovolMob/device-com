package ru.novolmob.devicecom.models

data class BasketModel(
    val list: List<DeviceModel> = emptyList(),
    val totalPrice: Double = 0.0,
) {
    val totalPriceString: String
        get() = "$totalPrice ₽"
}