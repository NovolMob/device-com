package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class AccessToken(
    val string: String
) {
    override fun toString(): String = string
}
