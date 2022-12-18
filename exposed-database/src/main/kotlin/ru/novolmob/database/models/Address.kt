package ru.novolmob.database.models

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class Address(
    val string: String
) {
    override fun toString(): String = string
}