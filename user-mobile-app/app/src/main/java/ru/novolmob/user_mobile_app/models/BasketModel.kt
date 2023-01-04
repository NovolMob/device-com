package ru.novolmob.user_mobile_app.models

data class BasketModel(
    val list: List<DeviceModel> = emptyList(),
    val totalPrice: Double = 0.0,
) {
    val totalPriceString: String
        get() = "$totalPrice ₽"
}