package ru.novolmob.devicecom.repositories

import arrow.core.Either
import io.ktor.client.*
import ru.novolmob.backend.ktorrouting.user.Devices
import ru.novolmob.backendapi.exceptions.AbstractBackendException
import ru.novolmob.backendapi.models.DeviceFullModel
import ru.novolmob.backendapi.repositories.IRepository
import ru.novolmob.core.models.ids.DeviceId
import ru.novolmob.devicecom.utils.KtorUtil.get

interface IDeviceRepository: IRepository {
    suspend fun get(deviceId: DeviceId): Either<AbstractBackendException, DeviceFullModel>
}

class DeviceRepositoryImpl(
    private val client: HttpClient
): IDeviceRepository {
    override suspend fun get(deviceId: DeviceId): Either<AbstractBackendException, DeviceFullModel> =
        client.get(Devices.Id(devices = Devices(), id = deviceId))

}