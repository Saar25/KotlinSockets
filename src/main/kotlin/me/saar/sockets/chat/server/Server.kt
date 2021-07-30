package me.saar.sockets.chat.server

import me.saar.sockets.MySocket
import me.saar.sockets.chat.Config
import me.saar.sockets.controller.ControllerAnalyzer
import me.saar.sockets.socketListen
import java.net.ServerSocket
import java.net.SocketException
import kotlin.concurrent.thread

class Server(port: Int) {

    private val serverController = ServerController()
    private val socketRouter = ControllerAnalyzer.buildRouter(this.serverController)

    private val socket = ServerSocket(port)

    private val clients = mutableListOf<MySocket>()

    private val clientsThread = thread(start = false) {
        try {
            loop()
        } catch (e: SocketException) {
            println("Goodbye")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    private val inputThread = thread(start = false) {
        var input = readLine()
        while (!input.equals("exit")) {
            if (input!!.startsWith("/say ")) {
                notifyClients(input.substring(5))
            } else {
                println("Write 'exit' to stop the server")
                println("Write '/say {message}' to send a message to clients")
            }
            input = readLine()
        }

        close()
    }

    private fun notifyClients(message: String) {
        this.clients.forEach { it.send(message) }
    }

    fun start() {
        this.clientsThread.start()
        this.inputThread.start()
    }

    private fun loop() {
        while (!this.socket.isClosed) {
            val socket = this.socket.accept()

            val client = MySocket(socket)

            socketListen(client) {
                this.socketRouter.onEvent(client, it)
                println("Request to '${it.endpoint}', with value '${it.body}'")
            }

            this.clients += client
        }
    }

    private fun close() {
        notifyClients(Config.SHUTDOWN)

        this.socket.close()
        this.clients.forEach { it.close() }
    }
}