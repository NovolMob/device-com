package storages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow

interface IStorage<T> {
    val key: String
    val storageScope: CoroutineScope
        get() = CoroutineScope(SupervisorJob())

    val value: StateFlow<T?>

    fun set(newValue: T?)

}