package me.saar.sockets

import com.github.saar25.kouter.RouteInput
import com.github.saar25.kouter.Router
import kotlin.concurrent.thread

class ClientSocketApp(private val socketRouter: Router<SocketRouteInput>) {

    fun start(host: String, port: Int, callback: (Client) -> Unit) = thread {
        val client = Client(host, port)
        callback.invoke(client)

        socketEventSubject(client).subscribe(
            onEvent = {
                val input = SocketRouteInput(client, it)
                this.socketRouter.handle(RouteInput(input.event.endpoint, input))
            },
            onClose = {
                println("ClientSocketApp Goodbye")
            }
        )
    }
}