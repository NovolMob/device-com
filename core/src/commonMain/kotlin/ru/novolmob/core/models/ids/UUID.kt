package ru.novolmob.core.models.ids


expect class UUID(mostSigBits: Long, leastSigBits: Long): Comparable<UUID> {
    val mostSigBits: Long
    val leastSigBits: Long

    override fun toString(): String
    companion object {
        fun randomUUID(): UUID
        fun fromString(string: String): UUID
        fun nameUUIDFromBytes(array: ByteArray): UUID
    }
}