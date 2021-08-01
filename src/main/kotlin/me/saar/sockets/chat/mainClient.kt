package me.saar.sockets.chat

import me.saar.sockets.ClientSocketApp
import me.saar.sockets.chat.client.AuthService
import me.saar.sockets.chat.client.ClientController
import me.saar.sockets.chat.client.ClientService
import me.saar.sockets.chat.shared.ChatStore
import me.saar.sockets.controller.buildRouter
import me.saar.sockets.splitAtIndex
import kotlin.concurrent.thread

private fun consoleInputThread(callback: (String, String) -> Boolean) = thread(start = true) {
    var shouldContinue = true
    while (shouldContinue) {
        val input = readLine()!!

        val index = input.indexOf(' ')
        shouldContinue = if (index == -1) {
            callback(input, "")
        } else {
            val (command, value) = input.splitAtIndex(index)
            callback(command, value)
        }
    }
}

fun main(args: Array<String>) {
    val chatStore = ChatStore()
    val authService = AuthService()

    val clientController = ClientController(chatStore, authService)

    val clientRouter = clientController.buildRouter()

    val clientSocketApp = ClientSocketApp(clientRouter)

    clientSocketApp.start(Config.HOST, Config.PORT) { socketService ->
        val clientService = ClientService(socketService, authService)

        clientService.join()

        consoleInputThread { command, value ->
            when (command) {
                "send" -> {
                    clientService.message(value)
                }
                "exit" -> {
                    clientService.exit()
                    clientSocketApp.close()
                }
                else -> {
                    println("Command not found")
                }
            }
            command != "exit"
        }

        chatStore.chatObservable.subscribe { e ->
            println(e.toMessage())
        }
    }
}