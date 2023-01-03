package ru.novolmob.backend.routings

import arrow.core.right
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Points
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.models.PointShortModel
import ru.novolmob.backendapi.repositories.IPointRepository

object PointRouting: KoinComponent {
    private val pointRepository: IPointRepository by inject()

    fun Route.pointRouting() {
        get<Points.ByCity> {
            val user = user()
            val either = user.city?.let {
                pointRepository.getByCity(city = it, language = user.language)
            } ?: emptyList<PointShortModel>().right()
            call.respond(either)
        }
        get<Points> {
            val user = user()
            val either = pointRepository.getAll(language = user.language)
            call.respond(either)
        }
        get<Points.Id> {
            val user = user()
            val either = pointRepository.getFull(pointId = it.id, language = user.language)
            call.respond(either)
        }
    }
}