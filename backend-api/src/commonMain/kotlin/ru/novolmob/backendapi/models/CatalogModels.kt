package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable

@Serializable
data class CatalogModel(
    val page: Int,
    val pageSize: Int,
    val devices: List<DeviceShortModel>,
    val amountOfPages: Int
)