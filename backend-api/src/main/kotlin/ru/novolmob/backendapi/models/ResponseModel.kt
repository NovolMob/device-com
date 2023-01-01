package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.backendapi.exceptions.BackendException

@Serializable
data class ResponseModel<T>(
    val data: T? = null,
    val exception: BackendException? = null
)