package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Features(
    val map: Map<String, String>
) {
    override fun toString(): String = map.toString()

    companion object {
        fun empty(): Features = Features(emptyMap())
        fun of(vararg pairs: Pair<String, String>): Features = Features(mapOf(*pairs))
    }

}