package me.saar.sockets

import java.net.ServerSocket
import java.net.SocketException
import kotlin.concurrent.thread

class ServerSocketApp(private val socketRouter: SocketRouter) {

    private var serverSocket: ServerSocket? = null
    private val clients = mutableListOf<MySocket>()

    fun start(port: Int, callback: () -> Unit = {}) {
        this.serverSocket = ServerSocket(port)
        callback()

        thread {
            this.serverSocket?.use {
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

    private fun loop(serverSocket: ServerSocket) {
        while (!serverSocket.isClosed) {
            val socket = serverSocket.accept()

            val client = MySocket(socket)

            socketListen(client) {
                val input = SocketRouteInput(client, it)
                this.socketRouter.handle(input)

                println("Request to '${it.endpoint}', with value '${it.body}'")
            }

            this.clients += client
        }
    }

    fun close() {
        this.serverSocket?.close()
        this.clients.forEach { it.close() }
    }
}