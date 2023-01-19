package ru.novolmob.backendapi.utils

object DatabaseAuthentication {
    private const val DATABASE_USER_PROPERTY = "DATABASE_USER"
    private const val DATABASE_PASSWORD_PROPERTY = "DATABASE_PASSWORD"
    private const val DATABASE_URL_PROPERTY = "DATABASE_URL"
    val url by lazy { System.getenv(DATABASE_URL_PROPERTY) ?: throw Exception("Database url not found") }

    fun readUser(): String? =
        System.getenv(DATABASE_USER_PROPERTY) ?: let {
            print("Enter database username: ")
            readln()
        }.takeIf { it.isNotEmpty() }
    fun readPassword(user: String): String? =
        System.getenv(DATABASE_PASSWORD_PROPERTY) ?: let {
            print("Enter password for $user: ")
            readln()
        }.takeIf { it.isNotEmpty() }
}