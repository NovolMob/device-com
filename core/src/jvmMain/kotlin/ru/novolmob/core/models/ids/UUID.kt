package ru.novolmob.core.models.ids

actual class UUID (val javaUUID: java.util.UUID) : Comparable<UUID> {
    actual val mostSigBits
        get() = javaUUID.mostSignificantBits
    actual val leastSigBits
        get() = javaUUID.leastSignificantBits

    actual constructor(mostSigBits: Long, leastSigBits: Long): this(java.util.UUID(mostSigBits, leastSigBits))

    override fun compareTo(other: UUID): Int = javaUUID.compareTo(other.javaUUID)

    actual override fun toString(): String = javaUUID.toString()

    actual companion object {
        actual fun randomUUID(): UUID = UUID(java.util.UUID.randomUUID())
        actual fun fromString(string: String): UUID = UUID(java.util.UUID.fromString(string))
        actual fun nameUUIDFromBytes(array: ByteArray): UUID = UUID(java.util.UUID.nameUUIDFromBytes(array))
        fun java.util.UUID.toUUID(): UUID = UUID(this)
    }
}