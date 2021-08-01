package me.saar.sockets

import java.net.SocketException
import kotlin.concurrent.thread

class ClientSocketApp(private val socketRouter: SocketRouter) : AutoCloseable {

    private var clientSocket: MySocket? = null

    fun start(host: String, port: Int, callback: (SocketService) -> Unit = {}) {
        this.clientSocket = MySocket(host, port)
        callback(SocketService(this.clientSocket!!))

        thread {
            this.clientSocket?.use {
                try {
                    loop(it)
                } catch (e: SocketException) {
                    println("Goodbye")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loop(socket: MySocket) {
        socketListen(socket) {
            val input = SocketRouteInput(socket, it)
            this.socketRouter.handle(input)
        }
    }

    val isClosed: Boolean get() = this.clientSocket?.isClosed == true

    override fun close() {
        this.clientSocket?.close()
    }
}