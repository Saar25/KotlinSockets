package me.saar.sockets.chat

import me.saar.sockets.chat.server.Server

fun main(args: Array<String>) {
    val server = Server(Config.PORT)
    server.start()
}