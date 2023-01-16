package services

import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import ru.novolmob.backendapi.exceptions.AbstractBackendException

interface IService {
    val serviceScope: CoroutineScope
        get() = CoroutineScope(SupervisorJob())
    val loading: StateFlow<Boolean>

    suspend fun update(): Either<AbstractBackendException, Any>
    suspend fun clear()
}