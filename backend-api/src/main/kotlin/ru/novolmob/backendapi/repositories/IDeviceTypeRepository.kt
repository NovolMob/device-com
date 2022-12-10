package ru.novolmob.backendapi.repositories

import ru.novolmob.backendapi.models.DeviceTypeCreateModel
import ru.novolmob.backendapi.models.DeviceTypeModel
import ru.novolmob.backendapi.models.DeviceTypeUpdateModel
import ru.novolmob.database.models.ids.DeviceTypeId

interface IDeviceTypeRepository: ICrudRepository<DeviceTypeId, DeviceTypeModel, DeviceTypeCreateModel, DeviceTypeUpdateModel>