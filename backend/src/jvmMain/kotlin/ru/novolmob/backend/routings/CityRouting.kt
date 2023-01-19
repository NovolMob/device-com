package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Cities
import ru.novolmob.backend.util.AuthUtil.get
import ru.novolmob.backend.util.AuthUtil.post
import ru.novolmob.backend.util.AuthUtil.put
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.models.CityCreateModel
import ru.novolmob.backendapi.models.CityUpdateModel
import ru.novolmob.backendapi.repositories.ICityRepository
import ru.novolmob.backendapi.repositories.IUserRepository
import ru.novolmob.backendapi.rights.Rights

object CityRouting: KoinComponent, IRouting {
    private val cityRepository: ICityRepository by inject()
    private val userRepository: IUserRepository by inject()

    override fun Route.generalRouting() {
        get<Cities> {
            val either = cityRepository.getAll(pagination = it, language = it.language)
            call.respond(either)
        }
    }

    override fun Route.routingForUser() {
        get<Cities.Id> {
            val user = user()
            val either = cityRepository.getFull(cityId = it.id, language = user.language)
            call.respond(either)
        }
    }

    override fun Route.routingForWorker() {
        get<ru.novolmob.backend.ktorrouting.worker.Cities>(Rights.Cities.Reading) {
            val either = cityRepository.getAll(it)
            call.respond(either)
        }
        post<ru.novolmob.backend.ktorrouting.worker.Cities>(Rights.Cities.Inserting) {
            val model: CityCreateModel = call.receive()
            val either = cityRepository.post(model)
            call.respond(either)
        }
        get<ru.novolmob.backend.ktorrouting.worker.Cities.Id>(Rights.Cities.Reading) {
            val either = cityRepository.get(it.id)
            call.respond(either)
        }
        post<ru.novolmob.backend.ktorrouting.worker.Cities.Id>(Rights.Cities.Updating) {
            val model: CityCreateModel = call.receive()
            val either = cityRepository.post(it.id, model)
            call.respond(either)
        }
        put<ru.novolmob.backend.ktorrouting.worker.Cities.Id>(Rights.Cities.Updating) {
            val model: CityUpdateModel = call.receive()
            val either = cityRepository.put(it.id, model)
            call.respond(either)
        }
        get<ru.novolmob.backend.ktorrouting.worker.Cities.Id>(Rights.Cities.Reading) {
            val either = cityRepository.get(it.id)
            call.respond(either)
        }
    }
}