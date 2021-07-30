package me.saar.sockets.chat

import me.saar.sockets.ServerSocketApp
import me.saar.sockets.chat.server.ChatStore
import me.saar.sockets.chat.server.ServerController
import me.saar.sockets.chat.shared.ChatServerMessage
import me.saar.sockets.controller.buildRouter
import kotlin.concurrent.thread

private fun inputThread(callback: () -> Unit) = thread {
    var input = readLine()
    while (!input.equals("/exit")) {
        if (input!!.startsWith("/say ")) {
            ChatStore.serverMessage(ChatServerMessage(input.substring(5)))
        } else {
            println("Write '/exit' to stop the server")
            println("Write '/say {message}' to send a message to clients")
        }
        input = readLine()
    }
    ChatStore.shutdown()

    callback()
}

fun main(args: Array<String>) {
    val serverController = ServerController()

    val socketRouter = serverController.buildRouter()

    val serverSocketApp = ServerSocketApp(socketRouter)

    serverSocketApp.start(Config.PORT) {
        println("Server started on port ${Config.PORT}")

        inputThread { serverSocketApp.close() }
    }
}