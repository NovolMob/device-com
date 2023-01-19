package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Cities
import ru.novolmob.backend.util.AuthUtil.delete
import ru.novolmob.backend.util.AuthUtil.get
import ru.novolmob.backend.util.AuthUtil.post
import ru.novolmob.backend.util.AuthUtil.put
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.models.CityCreateModel
import ru.novolmob.backendapi.models.CityDetailCreateModel
import ru.novolmob.backendapi.models.CityDetailUpdateModel
import ru.novolmob.backendapi.models.CityUpdateModel
import ru.novolmob.backendapi.repositories.ICityDetailRepository
import ru.novolmob.backendapi.repositories.ICityRepository
import ru.novolmob.backendapi.rights.Rights

object CityRouting: KoinComponent, IRouting {
    private val cityRepository: ICityRepository by inject()
    private val cityDetailRepository: ICityDetailRepository by inject()

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
        delete<ru.novolmob.backend.ktorrouting.worker.Cities.Id>(Rights.Cities.Deleting) {
            val either = cityRepository.delete(it.id)
            call.respond(either)
        }
        post<ru.novolmob.backend.ktorrouting.worker.Cities.Details>(Rights.Cities.Details.Updating) {
            val model: CityDetailCreateModel = call.receive()
            val either = cityDetailRepository.post(it.id, model)
            call.respond(either)
        }
        get<ru.novolmob.backend.ktorrouting.worker.Cities.Details>(Rights.Cities.Details.Reading) {
            val either = cityDetailRepository.get(it.id)
            call.respond(either)
        }
        put<ru.novolmob.backend.ktorrouting.worker.Cities.Details>(Rights.Cities.Details.Updating) {
            val model: CityDetailUpdateModel = call.receive()
            val either = cityDetailRepository.put(it.id, model)
            call.respond(either)
        }
        delete<ru.novolmob.backend.ktorrouting.worker.Cities.Details>(Rights.Cities.Details.Deleting) {
            val either = cityDetailRepository.delete(it.id)
            call.respond(either)
        }
        get<ru.novolmob.backend.ktorrouting.worker.Cities.Id.Details>(Rights.Cities.Details.Reading) {
            val either = cityDetailRepository.getDetailsFor(it.id.id)
            call.respond(either)
        }
        post<ru.novolmob.backend.ktorrouting.worker.Cities.Id.Details>(Rights.Cities.Details.Inserting) {
            val model: CityDetailCreateModel = call.receive()
            val either = cityDetailRepository.post(model.copy(parentId = it.id.id))
            call.respond(either)
        }
        delete<ru.novolmob.backend.ktorrouting.worker.Cities.Id.Details>(Rights.Cities.Details.Deleting) {
            val either = cityDetailRepository.removeDetailsFor(it.id.id)
            call.respond(either)
        }
    }
}