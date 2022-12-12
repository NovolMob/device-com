package ru.novolmob.backend

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.novolmob.backend.services.DatabaseService
import ru.novolmob.backend.util.NetworkUtil.respondException
import ru.novolmob.backendapi.exceptions.BackendExceptionCode

suspend fun main() {
    DatabaseService.connect()
        .onSuccess {
            embeddedServer(Netty, host = "0.0.0.0", port = 8080, module = Application::backend).start(true)
        }
        .onFailure {
            it.printStackTrace()
        }
}

fun Application.backend() {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception { call: ApplicationCall, cause: Exception ->
            call.respondException(
                code = BackendExceptionCode.UNKNOWN,
                message = cause.message ?: "Unknown error"
            )
        }
    }
    routing {
        get {
            call.respond("Hello world")
        }
    }
}