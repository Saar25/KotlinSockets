package me.saar.sockets

import kotlin.concurrent.thread

class ClientSocketApp(private val socketRouter: SocketRouter) : AutoCloseable {

    private var clientSocket: MySocket? = null

    fun start(host: String, port: Int, callback: (SocketService) -> Unit) = thread {
        this.clientSocket = MySocket(host, port)
        callback.invoke(SocketService(this.clientSocket!!))

        socketEventSubject(this.clientSocket!!).subscribe(
            onEvent = {
                val input = SocketRouteInput(this.clientSocket!!, it)
                this.socketRouter.handle(input)
            },
            onClose = {
                println("Goodbye")
            }
        )
    }

    val isClosed: Boolean get() = this.clientSocket?.isClosed == true

    override fun close() {
        this.clientSocket?.close()
    }
}