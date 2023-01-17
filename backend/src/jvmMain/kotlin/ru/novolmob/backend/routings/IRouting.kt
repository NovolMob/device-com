package ru.novolmob.backend.routings

import io.ktor.server.routing.*

interface IRouting {
    fun Route.generalRouting() {}
    fun Route.routingForUser() {}
    fun Route.routingForWorker() {}

}