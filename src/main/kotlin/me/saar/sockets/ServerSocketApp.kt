package me.saar.sockets

import kotlin.concurrent.thread

class ServerSocketApp(private val socketRouter: SocketRouter) {

    private val clients = mutableListOf<Client>()

    fun start(port: Int, callback: (Server) -> Unit) = thread {
        val server = Server(port)
        callback.invoke(server)

        whileNotClosed(server) {
            val client = server.accept()
            onSocketAccepted(client)
        }

        this.clients.forEach { it.close() }
        println("Goodbye")
    }

    private fun onSocketAccepted(client: Client) {
        this.clients += client

        socketEventSubject(client).subscribe(
            onEvent = {
                val input = SocketRouteInput(client, it)
                this.socketRouter.handle(input)

                println("Request to '${it.endpoint}', with value '${it.body}'")
            },
            onClose = {
                this.clients -= client
            }
        )
    }
}