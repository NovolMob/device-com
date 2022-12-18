package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.worker.Baskets
import ru.novolmob.backendapi.models.BasketCreateModel
import ru.novolmob.backendapi.models.BasketUpdateModel
import ru.novolmob.backendapi.repositories.IBasketRepository
import ru.novolmob.database.models.Amount

object BasketRouting: KoinComponent {
    private val basketRepository: IBasketRepository by inject()
    fun Routing.basketRouting() {
        get<Baskets> {
            call.respond(basketRepository.getAll(it))
        }
        post<Baskets> {
            val model: BasketCreateModel = call.receive()
            call.respond(basketRepository.post(model))
        }

        get<Baskets.Id> {
            call.respond(basketRepository.get(it.id))
        }
        post<Baskets.Id> {
            val model: BasketCreateModel = call.receive()
            call.respond(basketRepository.post(it.id, model))
        }
        put<Baskets.Id> {
            val model: BasketUpdateModel = call.receive()
            call.respond(basketRepository.put(it.id, model))
        }
        delete<Baskets.Id> {
            call.respond(basketRepository.delete(it.id))
        }

        get<Baskets.ByUserId> {
            call.respond(basketRepository.getBasket(it.userId))
        }

        post<Baskets.ByUserId.Devices.Id> {
            val amount: Amount = call.receive()
            call.respond(basketRepository.setInBasket(it.devices.byUserId.userId, it.deviceId, amount))
        }
        delete<Baskets.ByUserId.Devices.Id> {
            call.respond(basketRepository.removeFromBasket(it.devices.byUserId.userId, it.deviceId))
        }
        get<Baskets.ByUserId.Devices.Id> {
            call.respond(basketRepository.getAmount(it.devices.byUserId.userId, it.deviceId))
        }
    }
}