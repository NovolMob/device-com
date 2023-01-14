package ru.novolmob.jdbcdatabase.tables

object DdlCommandLogs: Table() {

    val event = text("event")
    val user = text("\"user\"")
    val userAddress = text("user_ip_address")
    val time = time("time")

}