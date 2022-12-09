package ru.novolmob.database.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Firstname(
    val string: String
) {
    override fun toString(): String = string
}