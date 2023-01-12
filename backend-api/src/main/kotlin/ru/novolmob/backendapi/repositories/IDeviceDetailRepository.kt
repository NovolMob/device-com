package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.DeviceDetailCreateModel
import ru.novolmob.backendapi.models.DeviceDetailModel
import ru.novolmob.backendapi.models.DeviceDetailUpdateModel
import ru.novolmob.core.models.ids.DeviceDetailId
import ru.novolmob.core.models.ids.DeviceId

interface IDeviceDetailRepository: IDetailRepository<DeviceDetailId, DeviceId, DeviceDetailModel, DeviceDetailCreateModel, DeviceDetailUpdateModel>