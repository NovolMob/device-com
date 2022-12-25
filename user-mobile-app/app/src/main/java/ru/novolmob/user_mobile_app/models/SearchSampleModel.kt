package ru.novolmob.user_mobile_app.models

import ru.novolmob.core.models.ids.DeviceTypeId

data class SearchSampleModel(
    val searchString: String = "",
    val deviceTypeId: DeviceTypeId? = null,
    val page: Int = 0,
    val pageSize: Int
)