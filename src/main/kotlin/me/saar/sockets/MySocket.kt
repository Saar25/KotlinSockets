package me.saar.sockets

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MySocket(private val socket: Socket) : AutoCloseable {

    constructor(host: String, port: Int) : this(Socket(host, port))

    private val output = PrintWriter(this.socket.getOutputStream(), true)

    private val input = BufferedReader(InputStreamReader(this.socket.getInputStream()))

    fun send(message: Int) = this.output.println(message)

    fun send(message: String) = this.output.println(message)

    fun send(event: SocketEvent) = this.output.println(event.toString())

    fun read(): String? = this.input.readLine()

    override fun close() = this.socket.close()

    val isClosed: Boolean get() = this.socket.isClosed
}