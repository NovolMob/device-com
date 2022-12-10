package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.DeviceDetailCreateModel
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.backendapi.models.DeviceDetailUpdateModel
import ru.novolmob.database.models.ids.DeviceDetailId

interface IDeviceDetailRepository: ICrudRepository<DeviceDetailId, DeviceDetailModel, DeviceDetailCreateModel, DeviceDetailUpdateModel>