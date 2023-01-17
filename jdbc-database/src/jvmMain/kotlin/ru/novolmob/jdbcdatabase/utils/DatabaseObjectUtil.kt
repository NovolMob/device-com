package ru.novolmob.jdbcdatabase.utils

object DatabaseObjectUtil {

    fun <T> Class<T>.tableName(): String =
        simpleName
            .replace("table", "", ignoreCase = true)
            .mapIndexed { index, c ->
                if (c.isUpperCase())
                    if (index == 0) c.lowercase() else "_${c.lowercase()}"
                else "$c"
            }.joinToString("")

    fun <T> Class<T>.viewName(): String =
        simpleName
            .mapIndexed { index, c ->
                if (c.isUpperCase())
                    if (index == 0) c.lowercase() else "_${c.lowercase()}"
                else "$c"
            }.joinToString("")

    fun <T> Class<T>.procedureName(): String =
        simpleName
            .mapIndexed { index, c ->
                if (c.isUpperCase())
                    if (index == 0) c.lowercase() else "_${c.lowercase()}"
                else "$c"
            }.joinToString("")

}