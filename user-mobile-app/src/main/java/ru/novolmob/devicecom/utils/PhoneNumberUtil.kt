package ru.novolmob.devicecom.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import ru.novolmob.core.models.PhoneNumber

object PhoneNumberOffsetMapping: OffsetMapping {
    private val format = "+% (%%%) %%%-%%-%%"
    private val transformed: List<Int> = transformed()
    private val origin: List<Int> = origin()

    private fun transformed(): List<Int> {
        var offset = 0
        val transformed = mutableListOf(0)
        format.forEach { c ->
            if (c == '%') {
                transformed.add(offset)
            } else {
                offset++
            }
        }
        transformed.removeAt(2)
        return transformed
    }

    private fun origin(): List<Int> {
        val origin = mutableListOf<Int>()
        transformed.forEachIndexed { index, i ->
            while (origin.size < index + i + 1) {
                origin.add(i)
            }
        }
        return origin
    }

    fun phoneNumber(phoneNumberString: String): String =
        if (phoneNumberString.isEmpty()) "" else
            buildString {
                append(format)
                phoneNumberString.forEach { char ->
                    indexOfFirst { it == '%' }.takeIf { it >= 0 }
                        ?.let { set(it, char) } ?: return@forEach
                }
                indexOfFirst { it == '%' }.takeIf { it >= 0 }
                    ?.let { index ->
                        substring(0, index).let {
                            clear()
                            append(it)
                        }
                    }
            }

    override fun originalToTransformed(offset: Int): Int {
        return transformed.getOrNull(offset)?.let(offset::plus) ?: origin.size
/*        if (offset <= 0) return offset
        if (offset <= 1) return offset +1
        if (offset <= 3) return offset +3
        if (offset <= 6) return offset +5
        if (offset <= 8) return offset +6
        if (offset <= 10) return offset +7
        return 18*/
    }

    override fun transformedToOriginal(offset: Int): Int {
        return origin.getOrNull(offset)?.let(offset::minus) ?: transformed.size
/*        if (offset <= 1) return offset
        if (offset <= 2) return offset -1
        if (offset <= 6) return offset -3
        if (offset <= 11) return offset -5
        if (offset <= 14) return offset -6
        if (offset <= 17) return offset -7
        return 11*/
    }

}

val PhoneNumberVisualTransformation: (AnnotatedString) -> TransformedText = { text: AnnotatedString ->
    val newText = PhoneNumberOffsetMapping.phoneNumber(text.text).let(::AnnotatedString)
    TransformedText(text = newText, offsetMapping = PhoneNumberOffsetMapping)
}

fun PhoneNumber.trim(): String = toString().replace(Regex("\\D"), "")