package ru.novolmob.backend.routings

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.ktorrouting.user.Orders
import ru.novolmob.backend.util.AuthUtil.user
import ru.novolmob.backend.util.KtorUtil.respond
import ru.novolmob.backendapi.repositories.IOrderRepository

object OrderRouting: KoinComponent {
    private val orderRepository: IOrderRepository by inject()

    fun Route.orderRouting() {
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
    }
}