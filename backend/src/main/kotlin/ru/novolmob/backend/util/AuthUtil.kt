package ru.novolmob.backend.util

import arrow.core.getOrElse
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.backendapi.repositories.IGrantedRightRepository
import ru.novolmob.core.models.AccessToken
import ru.novolmob.core.models.Code

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

    class WorkerPrincipal(var worker: WorkerModel): Principal
    class UserPrincipal(var user: UserModel): Principal

    fun PipelineContext<*, ApplicationCall>.user(user: UserModel) = call.principal<UserPrincipal>()?.apply {
        this.user = user
    }
    fun PipelineContext<*, ApplicationCall>.user(): UserModel = call.principal<UserPrincipal>()?.user
        ?: throw Exception("The sender is not authorized as a user.")

    fun PipelineContext<*, ApplicationCall>.worker(worker: WorkerModel) = call.principal<WorkerPrincipal>()?.apply {
        this.worker = worker
    }
    fun PipelineContext<*, ApplicationCall>.worker(): WorkerModel = call.principal<WorkerPrincipal>()?.worker
        ?: throw Exception("The sender is not authorized as a worker.")

    fun JWTCredential.user(): UserModel? =
        getClaim("user", String::class)?.let { Json.decodeFromString<UserModel>(it) }
    fun JWTCreator.Builder.user(user: UserModel) =
        withClaim("user", Json.encodeToString(user))
    fun JWTCredential.worker(): WorkerModel? =
        getClaim("worker", String::class)?.let { Json.decodeFromString<WorkerModel>(it) }
    fun JWTCreator.Builder.worker(worker: WorkerModel) =
        withClaim("worker", Json.encodeToString(worker))

    fun Application.authentication() {
        authentication {
            jwt {
                val jwtAudience = "jwtAudience"
                realm = "realm"
                verifier(
                    JWT
                        .require(Algorithm.HMAC256("secret"))
                        .withAudience(jwtAudience)
                        .withIssuer("domain")
                        .build()
                )
                validate { credential ->
                    credential.user()?.let {
                        UserPrincipal(it)
                    } ?: credential.worker()?.let {
                        WorkerPrincipal(it)
                    }
                }
            }
        }
    }

    fun UserModel.accessToken(): AccessToken = JWT.create()
        .withAudience("jwtAudience")
        .withIssuer("domain")
        .user(this)
        .sign(Algorithm.HMAC256("secret"))
        .let(::AccessToken)

    fun WorkerModel.accessToken(): AccessToken = JWT.create()
        .withAudience("jwtAudience")
        .withIssuer("domain")
        .worker(this)
        .sign(Algorithm.HMAC256("secret"))
        .let(::AccessToken)

    class WorkerAuthorizationPluginConfiguration: KoinComponent {
        var code: Code? = null
        val grantedRightRepository = AuthUtil.grantedRightRepository
    }
}