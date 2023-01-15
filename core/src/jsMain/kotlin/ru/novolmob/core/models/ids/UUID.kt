package ru.novolmob.core.models.ids

@JsModule("uuid")
external object UuidJsUtil {
    fun parse(string: String): IntArray
    fun v4(): String
    fun stringify(array: IntArray): String
}

fun List<Int>.toLong(): Long {
    var value = first().toLong()
    drop(1).forEach {
        value = (value shl 8) + it
    }
    return value
}

fun List<Byte>.toLong(): Long {
    var value = first().toLong()
    drop(1).forEach {
        value = (value shl 4) + it
    }
    return value
}

actual class UUID actual constructor(actual val mostSigBits: Long, actual val leastSigBits: Long) : Comparable<UUID> {

    constructor(intArray: IntArray): this(
        intArray.dropLast(8).toLong(),
        intArray.drop(8).toLong()
    )

    override fun compareTo(other: UUID): Int {
        return if (mostSigBits < other.mostSigBits) -1
            else if (mostSigBits > other.mostSigBits) 1
            else if (leastSigBits < other.leastSigBits) -1
            else if (leastSigBits > other.leastSigBits) 1 else 0
    }

    actual override fun toString(): String =
        buildString {
            for (i in 60 downTo 0 step 4) {
                val n = (mostSigBits shr i) and 15
                append(n.toString(16))
            }
            for (i in 60 downTo 0 step 4) {
                val n = (leastSigBits shr i) and 15
                append(n.toString(16))
            }
            insert(8, '-')
            insert(13, '-')
            insert(18, '-')
            insert(23, '-')
        }

    actual companion object {
        actual fun randomUUID(): UUID = fromString(UuidJsUtil.v4())

        actual fun fromString(string: String): UUID = UUID(UuidJsUtil.parse(string))

        actual fun nameUUIDFromBytes(array: ByteArray): UUID =
            array.toList().let { UUID(it.subList(0, 32).toLong(), it.subList(32, 64).toLong()) }
    }
}