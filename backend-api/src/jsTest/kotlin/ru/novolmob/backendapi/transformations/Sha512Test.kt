package ru.novolmob.backendapi.transformations

import ru.novolmob.core.models.Password
import kotlin.test.Test

class Sha512Test {

    @Test
    fun test() {
        PasswordTransformation().hash(Password("q1")).string.toList().joinToString { "$it" }
    }

}