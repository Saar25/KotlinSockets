package me.saar.sockets

import java.net.ServerSocket
import kotlin.concurrent.thread

class ServerSocketApp(private val socketRouter: SocketRouter) {

    private var serverSocket: ServerSocket? = null
    private val clients = mutableListOf<MySocket>()

    fun start(port: Int, callback: () -> Unit) = thread {
        this.serverSocket = ServerSocket(port)
        callback.invoke()

        whileSocketIsOpen(this.serverSocket!!) {
            val socket = this.serverSocket!!.accept()
            val client = MySocket(socket)
            onSocketAccepted(client)
        }

        println("Goodbye")
    }

    private fun onSocketAccepted(client: MySocket) {
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
        this.serverSocket?.close()
        this.clients.forEach { it.close() }
    }
}