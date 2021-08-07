package me.saar.sockets.chat

import me.saar.sockets.Client
import me.saar.sockets.ClientSocketApp
import me.saar.sockets.ConsoleApp
import me.saar.sockets.chat.client.AuthService
import me.saar.sockets.chat.client.ClientConsoleController
import me.saar.sockets.chat.client.ClientService
import me.saar.sockets.chat.client.ClientSocketController
import me.saar.sockets.chat.shared.ChatStore
import me.saar.sockets.controller.buildConsoleRouter
import me.saar.sockets.controller.buildSocketRouter

private fun buildConsoleApp(client: Client, clientService: ClientService): ConsoleApp {
    val controller = ClientConsoleController(client, clientService)

    val router = controller.buildConsoleRouter()

    return ConsoleApp(router)
}

private fun buildSocketApp(chatStore: ChatStore, authService: AuthService): ClientSocketApp {
    val socketController = ClientSocketController(chatStore, authService)

    val clientRouter = socketController.buildSocketRouter()

    return ClientSocketApp(clientRouter)
}

fun main(args: Array<String>) {
    val chatStore = ChatStore()
    val authService = AuthService()

    val clientSocketApp = buildSocketApp(chatStore, authService)

    clientSocketApp.start(Config.HOST, Config.PORT) { client ->
        val clientService = ClientService(client, authService)

        clientService.join()

        val clientConsoleApp = buildConsoleApp(client, clientService)

        clientConsoleApp.start("exit")

        chatStore.chatObservable.subscribe { e ->
            println(e.toMessage())
        }
    }
}