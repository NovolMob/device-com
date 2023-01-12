package ru.novolmob.jdbcdatabase.databases

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.PreparedStatement


open class Database {
    private val config = HikariConfig()
    private lateinit var ds: HikariDataSource
    private val connection: Connection
        get() = ds.connection
    fun connect(url: String, user: String, password: String): Database = apply {
        config.apply {
            this.jdbcUrl = url
            this.username = user
            this.password = password
            this.dataSourceProperties["cachePrepStmts"] = "true"
            this.dataSourceProperties["prepStmtCacheSize"] = "250"
            this.dataSourceProperties["prepStmtCacheSqlLimit"] = "2048"
        }
        ds = HikariDataSource(config)
    }
    suspend fun <T> transaction(block: suspend Connection.() -> T): T =
        connection.use { connection ->
            connection.autoCommit = false
            block(connection).also {
                connection.autoCommit = true
            }
        }

    suspend fun statement(sql: String) = transaction<Unit> { createStatement().use { it.execute(sql) } }

    suspend fun updateStatement(sql: String, block: suspend PreparedStatement.() -> Unit): Int =
        transaction {
            prepareStatement(sql).use {
                block(it)
                it.executeUpdate()
            }
        }

    suspend fun <T> queryStatement(sql: String, block: suspend PreparedStatement.() -> T) =
        transaction {
            prepareStatement(sql).use { block(it) }
        }

    suspend fun <T> callStatement(sql: String, block: suspend CallableStatement.() -> T): T =
        transaction {
            prepareCall(sql).use { block(it) }
        }

}