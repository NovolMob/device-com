package ru.novolmob.backend.util

import arrow.core.getOrElse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.backendapi.repositories.IGrantedRightRepository
import ru.novolmob.database.models.Code

object AuthUtil: KoinComponent {
    private val grantedRightRepository: IGrantedRightRepository by inject()

    private object AuthorizationRouteSelector: RouteSelector() {
        override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation =
            RouteSelectorEvaluation.Transparent
    }

    private val UserAuthorizationPlugin = createRouteScopedPlugin(
        name = "UserAuthorizationPlugin",
    ) {
        on(AuthenticationChecked) { call ->
            val principal = call.principal<UserPrincipal>()
            if (principal == null) {
                call.respondText(status = HttpStatusCode.Forbidden, text = "You are not authorized.")
            }
        }
    }


    private val WorkerAuthorizationPlugin = createRouteScopedPlugin(
        name = "WorkerAuthorizationPlugin",
        createConfiguration = ::WorkerAuthorizationPluginConfiguration
    ) {
        val requiredRight = pluginConfig.code
        val repository = pluginConfig.grantedRightRepository
        on(AuthenticationChecked) { call ->
            val principal = call.principal<WorkerPrincipal>()
            if (principal == null) {
                call.respondText(status = HttpStatusCode.Forbidden, text = "You are not authorized.")
                return@on
            } else if (requiredRight != null && !repository.contains(principal.worker.id, requiredRight).getOrElse { false }) {
                call.respondText(status = HttpStatusCode.Forbidden, text = "You don't have enough rights.")
                return@on
            }
        }
    }

    fun Route.workerPermission(code: Code? = null, block: Route.() -> Unit): Route =
        createChild(AuthorizationRouteSelector).apply {
            install(WorkerAuthorizationPlugin) {
                this.code = code
            }
            block()
        }
    fun Route.userPermission(block: Route.() -> Unit): Route =
        createChild(AuthorizationRouteSelector).apply {
            install(UserAuthorizationPlugin)
            block()
        }

    class WorkerPrincipal(val worker: WorkerModel): Principal
    class UserPrincipal(val user: UserModel): Principal

    fun ApplicationCall.user(): UserModel = principal<UserPrincipal>()?.user ?: throw Exception("The sender is not authorized as a user.")
    fun ApplicationCall.worker(): WorkerModel = principal<WorkerPrincipal>()?.worker ?: throw Exception("The sender is not authorized as a worker.")

    class WorkerAuthorizationPluginConfiguration: KoinComponent {
        var code: Code? = null
        val grantedRightRepository = AuthUtil.grantedRightRepository
    }
}