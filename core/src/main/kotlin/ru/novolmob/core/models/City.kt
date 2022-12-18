package ru.novolmob.core.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class City(
    val string: String
) {
    override fun toString(): String = string
}