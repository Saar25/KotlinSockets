package me.saar.sockets

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MySocket(private val socket: Socket) {

    private val output = PrintWriter(this.socket.getOutputStream(), true)

    private val input = BufferedReader(InputStreamReader(this.socket.getInputStream()))

    fun send(message: Int) = this.output.println(message)

    fun send(message: String) = this.output.println(message)

    fun read(): String? = this.input.readLine()

    fun close() = this.socket.close()

    val isClosed: Boolean get() = this.socket.isClosed
}