package me.saar.sockets

import kotlin.concurrent.thread

class ServerSocketApp(private val socketRouter: SocketRouter) {

    private var server: Server? = null
    private val clients = mutableListOf<Client>()

    fun start(port: Int, callback: () -> Unit) = thread {
        this.server = Server(port)
        callback.invoke()

        whileNotClosed(this.server!!) {
            val client = this.server!!.accept()
            onSocketAccepted(client)
        }

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

    fun close() {
        this.server?.close()
        this.clients.forEach { it.close() }
    }
}