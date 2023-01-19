package ru.novolmob.backend.routings

import arrow.core.right
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Points
import ru.novolmob.backend.util.AuthUtil.get
import ru.novolmob.backend.util.AuthUtil.post
import ru.novolmob.backend.util.AuthUtil.put
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.models.PointCreateModel
import ru.novolmob.backendapi.models.PointShortModel
import ru.novolmob.backendapi.models.PointUpdateModel
import ru.novolmob.backendapi.repositories.IPointRepository
import ru.novolmob.backendapi.rights.Rights

object PointRouting: KoinComponent, IRouting {
    private val pointRepository: IPointRepository by inject()

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
        post<ru.novolmob.backend.ktorrouting.worker.Points>(Rights.Points.Reading) {
            val model: PointCreateModel = call.receive()
            val either = pointRepository.post(model)
            call.respond(either)
        }
        get<ru.novolmob.backend.ktorrouting.worker.Points.Id>(Rights.Points.Reading) {
            val either = pointRepository.get(it.id)
            call.respond(either)
        }
        post<ru.novolmob.backend.ktorrouting.worker.Points.Id>(Rights.Points.Reading) {
            val model: PointCreateModel = call.receive()
            val either = pointRepository.post(it.id, model)
            call.respond(either)
        }
        put<ru.novolmob.backend.ktorrouting.worker.Points.Id>(Rights.Points.Reading) {
            val model: PointUpdateModel = call.receive()
            val either = pointRepository.put(it.id, model)
            call.respond(either)
        }
    }
}