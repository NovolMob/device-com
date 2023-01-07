package ru.novolmob.user_mobile_app.services

import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.novolmob.backendapi.exceptions.AbstractBackendException

interface IService {
    val serviceScope: CoroutineScope
        get() = CoroutineScope(SupervisorJob())

    suspend fun update(): Either<AbstractBackendException, Any>
}