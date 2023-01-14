package ru.novolmob.backendapi.exceptions

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
sealed class AbstractBackendException {
    abstract val code: BackendExceptionCode
    abstract val message: String

    @Serializable
    @SerialName("simple")
    class BackendException(
        override val code: BackendExceptionCode,
        override val message: String,
    ): AbstractBackendException()

    @Serializable
    @SerialName("payload")
    class PayloadBackendException(
        override val code: BackendExceptionCode,
        override val message: String
    ): AbstractBackendException() {
        var payload: JsonElement? = null

        inline fun <reified Payload> payload(payload: Payload? = null): PayloadBackendException = apply {
            this.payload = json.encodeToJsonElement(payload)
        }

        inline fun <reified Payload> payload(): Payload? = payload?.let { json.decodeFromJsonElement(it) }

        companion object {
            val json = Json {
                encodeDefaults = false
                ignoreUnknownKeys = true
            }
        }
    }

}