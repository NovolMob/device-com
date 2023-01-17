package ru.novolmob.backendapi.models

import kotlinx.serialization.Serializable
import ru.novolmob.core.models.AccessToken

@Serializable
data class TokenModel(
    val accessToken: AccessToken
)