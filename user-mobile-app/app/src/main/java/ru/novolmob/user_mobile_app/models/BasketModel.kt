package ru.novolmob.user_mobile_app.models

class BasketModel(
    val list: List<DeviceModel> = emptyList(),
    val totalPrice: Double = 0.0,
    val totalPriceString: String = "$totalPrice ₽"
)