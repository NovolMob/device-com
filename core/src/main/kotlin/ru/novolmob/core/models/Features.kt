package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Features(
    val map: Map<String, String>
) {
    override fun toString(): String = map.toString()
}