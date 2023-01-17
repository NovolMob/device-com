package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.exceptions.AbstractBackendException

@Serializable
data class ResponseModel<T>(
    val data: T? = null,
    val exception: AbstractBackendException? = null
)