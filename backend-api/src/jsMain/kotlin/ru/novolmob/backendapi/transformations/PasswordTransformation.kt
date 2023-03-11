package ru.novolmob.backendapi.transformations

import ru.novolmob.core.models.Password

@JsModule("js-sha512")
@JsNonModule
external object Sha512Util {
    fun sha512(string: String): String
}

actual class PasswordTransformation : Hashing<Password> {
    override fun hash(originalValue: Password): Password =
        Sha512Util.sha512(originalValue.string).uppercase().let(::Password)

}