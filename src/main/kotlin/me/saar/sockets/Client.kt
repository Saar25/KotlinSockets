package me.saar.sockets

import com.beust.klaxon.Klaxon
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Client(private val socket: Socket) : CloseableState {

    constructor(host: String, port: Int) : this(Socket(host, port))

    private val output = PrintWriter(this.socket.getOutputStream(), true)

    private val input = BufferedReader(InputStreamReader(this.socket.getInputStream()))

    fun readEvent() = this.input.readLine()?.let { SocketEvent.parse(it) }

    fun sendEvent(event: SocketEvent) = this.output.println(event.toString())

    fun sendJson(endpoint: String, body: Any?) {
        val json = Klaxon().toJsonString(body)
        sendEvent(SocketEvent(endpoint, json))
    }

    override val isClosed: Boolean get() = this.socket.isClosed

    override fun close() = this.socket.close()

}