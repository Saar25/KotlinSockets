package me.saar.sockets.chat

import me.saar.sockets.ServerSocketApp
import me.saar.sockets.chat.server.ServerController
import me.saar.sockets.chat.shared.ChatServerMessage
import me.saar.sockets.chat.shared.ChatStore
import me.saar.sockets.controller.buildSocketRouter
import kotlin.concurrent.thread

private val chatStore = ChatStore()

private fun inputThread(callback: () -> Unit) = thread {
    var input = readLine()
    while (!input.equals("/exit")) {
        if (input!!.startsWith("/say ")) {
            chatStore.serverMessage(ChatServerMessage(input.substring(5)))
        } else {
            println("Write '/exit' to stop the server")
            println("Write '/say {message}' to send a message to clients")
        }
        input = readLine()
    }
    chatStore.shutdown()

    callback()
}

fun main(args: Array<String>) {
    val serverController = ServerController(chatStore)

    val socketRouter = serverController.buildSocketRouter()

    val serverSocketApp = ServerSocketApp(socketRouter)

    serverSocketApp.start(Config.PORT) { server ->
        println("Server started on port ${Config.PORT}")

        inputThread { server.close() }
    }
}