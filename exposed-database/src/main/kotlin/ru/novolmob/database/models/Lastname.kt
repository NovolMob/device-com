package ru.novolmob.database.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Lastname(
    val string: String
) {
    override fun toString(): String = string
}