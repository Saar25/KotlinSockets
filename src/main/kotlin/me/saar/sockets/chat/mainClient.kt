package me.saar.sockets.chat

import me.saar.sockets.chat.client.Client

fun main(args: Array<String>) {
    val client = Client(Config.HOST, Config.PORT)
    client.start()
}