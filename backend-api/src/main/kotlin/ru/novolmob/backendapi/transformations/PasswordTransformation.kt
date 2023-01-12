package ru.novolmob.backendapi.transformations

import ru.novolmob.core.models.Password
import java.math.BigInteger
import java.security.MessageDigest

class PasswordTransformation: Hashing<Password> {
    private val messageDigest = MessageDigest.getInstance("SHA-512")
    override fun hash(originalValue: Password): Password =
        messageDigest
            .digest(originalValue.string.encodeToByteArray())
            .let { BigInteger(1, it).toString(16) }
            .let { Password((if (it.length < 32) "0$it" else it).uppercase()) }
}