package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Devices
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.repositories.IDeviceRepository
import ru.novolmob.backendapi.repositories.IDeviceTypeRepository

object DevicesRouting: KoinComponent {
    private val deviceRepository: IDeviceRepository by inject()
    private val deviceTypeRepository: IDeviceTypeRepository by inject()

    fun Route.deviceRouting() {
        get<Devices.Id> {
            val user = user()
            val either = deviceRepository.getFull(deviceId = it.id, language = user.language)
            call.respond(either = either)
        }
        get<Devices.Types> {
            val user = user()
            val either = deviceTypeRepository.getAll(pagination = it, language = user.language)
            call.respond(either = either)
        }
        get<Devices.Types.Id> {
            val user = user()
            val either = deviceTypeRepository.getFull(deviceTypeId = it.id, language = user.language)
            call.respond(either = either)
        }
    }
}