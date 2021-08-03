package me.saar.sockets

import kotlin.concurrent.thread

class ClientSocketApp(private val socketRouter: SocketRouter) : AutoCloseable {

    private var client: Client? = null

    fun start(host: String, port: Int, callback: (SocketService) -> Unit) = thread {
        this.client = Client(host, port)
        callback.invoke(SocketService(this.client!!))

        socketEventSubject(this.client!!).subscribe(
            onEvent = {
                val input = SocketRouteInput(this.client!!, it)
                this.socketRouter.handle(input)
            },
            onClose = {
                println("Goodbye")
            }
        )
    }

    val isClosed: Boolean get() = this.client?.isClosed == true

    override fun close() {
        this.client?.close()
    }
}