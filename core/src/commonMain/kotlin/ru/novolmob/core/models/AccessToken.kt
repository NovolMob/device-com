package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class AccessToken(
    val string: String
) {
    override fun toString(): String = string
}
