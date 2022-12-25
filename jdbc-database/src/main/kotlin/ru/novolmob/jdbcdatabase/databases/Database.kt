package ru.novolmob.jdbcdatabase.databases

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

open class Database {
    private lateinit var connection: Connection
    fun connect(url: String, user: String, password: String): Database = apply {
        connection = DriverManager.getConnection(url, user, password)
    }
    fun <T> transaction(block: Connection.() -> T): T =
        connection.run {
            autoCommit = false
            block().also {
                autoCommit = true
            }
        }

    fun statement(sql: String) = transaction<Unit> { createStatement().execute(sql) }

    fun updateStatement(sql: String, block: PreparedStatement.() -> Unit): Int =
        transaction {
            prepareStatement(sql).apply(block).executeUpdate()
        }

    fun queryStatement(sql: String, block: PreparedStatement.() -> Unit): ResultSet =
        transaction {
            prepareStatement(sql).apply(block).executeQuery()
        }

}