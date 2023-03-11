package ru.novolmob.core.models

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class Schedule(
    val map: Map<String, String>
) {
    override fun toString(): String = map.toString()

    companion object {
        fun empty(): Schedule = Schedule(emptyMap())
        fun of(vararg pairs: Pair<String, String>): Schedule = Schedule(mapOf(*pairs))
    }

}