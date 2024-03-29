package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Orders
import ru.novolmob.backend.utils.AuthUtil.user
import ru.novolmob.backend.utils.KtorUtil.respond
import ru.novolmob.backendapi.repositories.IOrderRepository

object OrderRouting: KoinComponent, IRouting {
    private val orderRepository: IOrderRepository by inject()

    override fun Route.routingForUser() {
        get<Orders> {
            val user = user()
            val either = orderRepository.getOrdersFor(userId = user.id, language = user.language)
            call.respond(either = either)
        }
        get<Orders.Id> {
            val user = user()
            val either = orderRepository.getFull(orderId = it.id, language = user.language)
            call.respond(either = either)
        }
        post<Orders.Confirm> {
            val user = user()
            val either = orderRepository.confirmOrder(userId = user.id, pointId = it.pointId, language = user.language)
            call.respond(either = either)
        }
        delete<Orders.Id> {
            val either = orderRepository.cancelOrder(it.id)
            call.respond(either = either)
        }
    }
}