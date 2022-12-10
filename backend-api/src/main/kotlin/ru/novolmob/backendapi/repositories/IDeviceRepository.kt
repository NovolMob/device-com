package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.DeviceCreateModel
import ru.novolmob.backendapi.models.DeviceModel
import ru.novolmob.backendapi.models.DeviceUpdateModel
import ru.novolmob.database.models.ids.DeviceId

interface IDeviceRepository: ICrudRepository<DeviceId, DeviceModel, DeviceCreateModel, DeviceUpdateModel>