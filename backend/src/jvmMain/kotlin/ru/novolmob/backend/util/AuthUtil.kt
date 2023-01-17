package ru.novolmob.backend.util

import arrow.core.getOrElse
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.novolmob.backend.util.KtorUtil.respondException
import ru.novolmob.backendapi.exceptions.dontHaveRightsException
import ru.novolmob.backendapi.exceptions.notAuthorizedException
import ru.novolmob.backendapi.models.UserModel
import ru.novolmob.backendapi.models.WorkerModel
import ru.novolmob.backendapi.repositories.IGrantedRightRepository
import ru.novolmob.backendapi.repositories.IUserRepository
import ru.novolmob.backendapi.repositories.IWorkerRepository
import ru.novolmob.backendapi.rights.Rights
import ru.novolmob.core.models.AccessToken

object AuthUtil: KoinComponent {
    private val grantedRightRepository: IGrantedRightRepository by inject()
    private val userRepository: IUserRepository by inject()
    private val workerRepository: IWorkerRepository by inject()

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
                call.respondException(notAuthorizedException())
                return@on
            }
        }
    }


    private val WorkerAuthorizationPlugin = createRouteScopedPlugin(
        name = "WorkerAuthorizationPlugin",
        createConfiguration = ::WorkerAuthorizationPluginConfiguration
    ) {
        val requiredRight = pluginConfig.right
        val repository = pluginConfig.grantedRightRepository
        on(AuthenticationChecked) { call ->
            val principal = call.principal<WorkerPrincipal>()
            if (principal == null) {
                call.respondException(notAuthorizedException())
                return@on
            } else if (requiredRight != null && !repository.containsAny(principal.worker.id, requiredRight.tree).getOrElse { false }) {
                call.respondException(dontHaveRightsException())
                return@on
            }
        }
    }

    fun Route.workerPermission(right: Rights? = null, block: Route.() -> Unit): Route =
        createChild(AuthorizationRouteSelector).apply {
            install(WorkerAuthorizationPlugin) {
                this.right = right
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

    suspend fun JWTCredential.user(): UserModel? =
        getClaim("userId", String::class)?.let { userRepository.get(Json.decodeFromString(it)).orNull() }
    fun JWTCreator.Builder.user(user: UserModel) =
        withClaim("userId", Json.encodeToString(user.id))
    suspend fun JWTCredential.worker(): WorkerModel? =
        getClaim("workerId", String::class)?.let { workerRepository.get(Json.decodeFromString(it)).orNull() }
    fun JWTCreator.Builder.worker(worker: WorkerModel) =
        withClaim("workerId", Json.encodeToString(worker.id))

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
        var right: Rights? = null
        val grantedRightRepository = AuthUtil.grantedRightRepository
    }
}