package utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import react.StateSetter
import react.useEffect
import react.useState
import storages.IStorage

fun <T> StateFlow<T>.useFlow(): Pair<T, StateSetter<T>> {
    val (value, setValue) = useState(this.value)
    useEffect {
        onEach { setValue(it) }.launchIn(CoroutineScope(SupervisorJob()))
    }
    return value to setValue
}

fun <T> IStorage<T>.useFlow() = value.useFlow()