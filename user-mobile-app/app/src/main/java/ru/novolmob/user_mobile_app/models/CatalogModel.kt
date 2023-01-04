package ru.novolmob.user_mobile_app.models

data class CatalogModel(
    val page: Int = 0,
    val pageSize: Int = 0,
    val list: List<DeviceModel> = emptyList(),
    val amountOfPage: Int = 0
)