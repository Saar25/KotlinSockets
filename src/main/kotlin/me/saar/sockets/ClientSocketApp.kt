package me.saar.sockets

import kotlin.concurrent.thread

class ClientSocketApp(private val socketRouter: SocketRouter) {

    fun start(host: String, port: Int, callback: (Client) -> Unit) = thread {
        val client = Client(host, port)
        callback.invoke(client)

        socketEventSubject(client).subscribe(
            onEvent = {
                val input = SocketRouteInput(client, it)
                this.socketRouter.handle(input)
            },
            onClose = {
                println("Goodbye")
            }
        )
    }
}