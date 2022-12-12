package ru.novolmob.backendapi.exceptions

import kotlinx.serialization.Serializable

@Serializable
class BackendException(
    val code: BackendExceptionCode,
    val message: String
)