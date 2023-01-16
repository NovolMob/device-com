package storages

import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

open class SerializableStorage<T>(
    name: String? = null,
    val serializer: KSerializer<T>,
    val json: Json = Json
): IStorage<T> {
    override val key: String = name ?: serializer.descriptor.serialName
    private val _value = MutableStateFlow<T?>(null)
    override val value: StateFlow<T?> = _value.asStateFlow()

    init {
        storageScope.launch {
            launch {
                set(getFromLocalStorage())
            }
            launch {
                value.collectLatest { value ->
                    setInLocalStorage(value)
                }
            }
        }
    }

    private fun getFromLocalStorage(): T? =
        localStorage.getItem(key)?.let { string ->
            kotlin.runCatching {
                json.decodeFromString(serializer, string)
            }.getOrNull()
        }

    private fun setInLocalStorage(newValue: T?) =
        newValue?.let {
            localStorage.setItem(key, json.encodeToString(serializer, it))
        } ?: removeFromLocalStorage()

    private fun removeFromLocalStorage() = localStorage.removeItem(key)

    override fun set(newValue: T?) {
        _value.update { newValue }
    }
}