package ru.novolmob.backendapi.models

data class CatalogModel(
    val page: Int,
    val pageSize: Int,
    val devices: List<DeviceShortModel>,
    val amountOfPages: Int
)