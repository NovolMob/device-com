package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Schedule(
    val map: Map<String, String>
) {
    override fun toString(): String = map.toString()
}