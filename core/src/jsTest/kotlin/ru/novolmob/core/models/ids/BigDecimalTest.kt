package ru.novolmob.core.models.ids

import ru.novolmob.core.models.BigDecimal
import ru.novolmob.core.models.BigDecimal.Companion.toBigDecimal
import kotlin.test.Test

class BigDecimalTest {

    @Test
    fun create() {
        val i = 1
        val l = 1123L
        val d = 11222.123
        val f = 11222.223f
        println(i.toBigDecimal())
        println(l.toBigDecimal())
        println(d.toBigDecimal())
        println(BigDecimal.ZERO)
    }

}