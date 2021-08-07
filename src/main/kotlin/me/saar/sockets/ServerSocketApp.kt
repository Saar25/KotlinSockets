package me.saar.sockets

import me.saar.kouter.RouteInput
import me.saar.kouter.Router
import kotlin.concurrent.thread

class ServerSocketApp(private val socketRouter: Router<SocketRouteInput>) {

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
                this.socketRouter.handle(RouteInput(input.event.endpoint, input))

                println("Request to '${it.endpoint}', with value '${it.body}'")
            },
            onClose = {
                this.clients -= client
            }
        )
    }
}