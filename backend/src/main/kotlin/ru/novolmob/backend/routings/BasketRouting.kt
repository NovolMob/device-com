package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Basket
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.repositories.IBasketRepository

object BasketRouting: KoinComponent {
    private val basketRepository: IBasketRepository by inject()

    fun Route.basketRouting() {
        get<Basket> {
            val user = user()
            val either = basketRepository.getBasket(userId = user.id, language = user.language)
            call.respond(either = either)
        }
        post<Basket.Device> {
            val user = user()
            val either = basketRepository.setInBasket(userId = user.id, deviceId = it.deviceId, amount = call.receive())
            call.respond(either = either)
        }
        delete<Basket.Device> {
            val user = user()
            val either = basketRepository.removeFromBasket(userId = user.id, deviceId = it.deviceId)
            call.respond(either = either)
        }
        get<Basket.Device> {
            val user = user()
            val either = basketRepository.getAmount(userId = user.id, deviceId = it.deviceId)
            call.respond(either = either)
        }
    }

}