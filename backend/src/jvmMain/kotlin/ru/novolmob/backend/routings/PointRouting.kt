package ru.novolmob.backend.routings

import arrow.core.right
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Points
import ru.novolmob.backend.util.AuthUtil.delete
import ru.novolmob.backend.util.AuthUtil.get
import ru.novolmob.backend.util.AuthUtil.post
import ru.novolmob.backend.util.AuthUtil.put
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.models.*
import ru.novolmob.backendapi.repositories.IPointDetailRepository
import ru.novolmob.backendapi.repositories.IPointRepository
import ru.novolmob.backendapi.rights.Rights

object PointRouting: KoinComponent, IRouting {
    private val pointRepository: IPointRepository by inject()
    private val pointDetailRepository: IPointDetailRepository by inject()

    override fun Route.routingForUser() {
        get<Points.ByCity> {
            val user = user()
            val either = user.cityId?.let {
                pointRepository.getByCity(cityId = it, language = user.language)
            } ?: emptyList<PointShortModel>().right()
            call.respond(either)
        }
        get<Points> {
            val user = user()
            val either = pointRepository.getAll(pagination = it, language = user.language)
            call.respond(either)
        }
        get<Points.Id> {
            val user = user()
            val either = pointRepository.getFull(id = it.id, language = user.language)
            call.respond(either)
        }
    }

    override fun Route.routingForWorker() {
        get<ru.novolmob.backend.ktorrouting.worker.Points>(Rights.Points.Reading) {
            val either = pointRepository.getAll(it)
            call.respond(either)
        }
        post<ru.novolmob.backend.ktorrouting.worker.Points>(Rights.Points.Inserting) {
            val model: PointCreateModel = call.receive()
            val either = pointRepository.post(model)
            call.respond(either)
        }
        get<ru.novolmob.backend.ktorrouting.worker.Points.Id>(Rights.Points.Reading) {
            val either = pointRepository.get(it.id)
            call.respond(either)
        }
        post<ru.novolmob.backend.ktorrouting.worker.Points.Id>(Rights.Points.Updating) {
            val model: PointCreateModel = call.receive()
            val either = pointRepository.post(it.id, model)
            call.respond(either)
        }
        put<ru.novolmob.backend.ktorrouting.worker.Points.Id>(Rights.Points.Updating) {
            val model: PointUpdateModel = call.receive()
            val either = pointRepository.put(it.id, model)
            call.respond(either)
        }
        delete<ru.novolmob.backend.ktorrouting.worker.Points.Id>(Rights.Points.Deleting) {
            val either = pointRepository.delete(it.id)
            call.respond(either)
        }
        get<ru.novolmob.backend.ktorrouting.worker.Points.Id.Details>(Rights.Points.Details.Reading) {
            val either = pointDetailRepository.getDetailsFor(it.id.id)
            call.respond(either)
        }
        put<ru.novolmob.backend.ktorrouting.worker.Points.Id.Details>(Rights.Points.Inserting) {
            val model: PointDetailCreateModel = call.receive()
            val either = pointDetailRepository.post(model.copy(parentId = it.id.id))
            call.respond(either)
        }
        delete<ru.novolmob.backend.ktorrouting.worker.Points.Id.Details>(Rights.Points.Details.Deleting) {
            val either = pointDetailRepository.removeDetailsFor(it.id.id)
            call.respond(either)
        }
        get<ru.novolmob.backend.ktorrouting.worker.Points.Detail>(Rights.Points.Details.Reading) {
            val either = pointDetailRepository.get(it.id)
            call.respond(either)
        }
        post<ru.novolmob.backend.ktorrouting.worker.Points.Detail>(Rights.Points.Details.Updating) {
            val model: PointDetailCreateModel = call.receive()
            val either = pointDetailRepository.post(it.id, model)
            call.respond(either)
        }
        put<ru.novolmob.backend.ktorrouting.worker.Points.Detail>(Rights.Points.Details.Updating) {
            val model: PointDetailUpdateModel = call.receive()
            val either = pointDetailRepository.put(it.id, model)
            call.respond(either)
        }
        delete<ru.novolmob.backend.ktorrouting.worker.Points.Detail>(Rights.Points.Details.Deleting) {
            val either = pointDetailRepository.delete(it.id)
            call.respond(either)
        }
    }
}