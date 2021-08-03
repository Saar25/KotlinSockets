package me.saar.sockets

import java.net.ServerSocket

class Server(private val serverSocket: ServerSocket) : CloseableState {

    constructor(port: Int) : this(ServerSocket(port))

    fun accept() = Client(this.serverSocket.accept())

    override val isClosed: Boolean get() = this.serverSocket.isClosed

    override fun close() = this.serverSocket.close()
}