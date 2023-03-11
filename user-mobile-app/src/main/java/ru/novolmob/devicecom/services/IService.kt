package ru.novolmob.devicecom.services

import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.novolmob.backendapi.exceptions.AbstractBackendException

interface IService {
    val serviceScope: CoroutineScope
        get() = CoroutineScope(SupervisorJob())

    suspend fun update(): Either<AbstractBackendException, Any>
    suspend fun clear()
}