package ru.novolmob.user_mobile_app.models

data class CatalogModel(
    val list: List<DeviceModel> = emptyList(),
    val amountOfPage: Int = 0
)