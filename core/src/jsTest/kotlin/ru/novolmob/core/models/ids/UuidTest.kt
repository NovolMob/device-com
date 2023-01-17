package ru.novolmob.core.models.ids

import kotlin.test.Test
import kotlin.test.assertTrue


class UuidTest {

    @Test
    fun parse() {
        val string = "9a657e52-98f1-4fd4-90ed-a9ec8ea54a35"
        val uuid = UUID.fromString(string)
        assertTrue("$uuid is wrong") { uuid.toString().length == 36 }
        assertTrue("$uuid not equal $string") { uuid.toString() == string }
    }
    @Test
    fun v4() {
        val uuid = UUID.randomUUID()
        assertTrue("$uuid is wrong") { uuid.toString().length == 36 }
    }

    @Test
    fun stringify() {
        val first = UuidJsUtil.parse("9a657e52-98f1-4fd4-90ed-a9ec8ea54a35")
        println(first)
    }

}