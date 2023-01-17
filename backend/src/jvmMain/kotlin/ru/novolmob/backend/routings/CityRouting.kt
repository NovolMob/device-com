package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Cities
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.repositories.ICityRepository

object CityRouting: KoinComponent, IRouting {
    private val cityRepository: ICityRepository by inject()

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
}