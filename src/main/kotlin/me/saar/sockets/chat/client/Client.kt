package me.saar.sockets.chat.client

import me.saar.sockets.MySocket
import me.saar.sockets.chat.shared.ChatLeave
import me.saar.sockets.chat.shared.ChatMessage
import me.saar.sockets.chat.shared.ChatShutdown
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread

class Client(host: String, port: Int) {

    class Command(val endpoint: String, val action: (String) -> Unit)

    private var clientId: Int = -1

    private val socket = MySocket(Socket(host, port))

    private val clientService = ClientService(this.socket)

    private val commands = listOf(
        Command("exit") {
            val body = ChatLeave(this.clientId)
            this.clientService.exit(body)

            close()
        },

        Command("send") { value: String ->
            val content = value.substring("send".length + 1)
            val body = ChatMessage(this.clientId, content)
            this.clientService.message(body)
        },
    )

    private val inputThread = thread(start = false) {
        try {
            var message = this.socket.read()
            while (!message.equals(ChatShutdown.toMessage())) {
                println(message)

                message = this.socket.read()
            }
            println(message)
        } catch (e: SocketException) {
            println("Goodbye")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    private val outputThread = thread(start = false) {
        println("Commands: ${this.commands.joinToString { it.endpoint }}")
        while (!this.socket.isClosed) {
            val input = readLine()!!

            val command = this.commands.find { c -> input.startsWith(c.endpoint) }
            command?.action?.invoke(input) ?: println("Command not found")
        }
    }


    fun start() {
        thread {
            this.clientService.join()

            this.clientId = this.socket.read()!!.toInt()
            println("I am ${this.clientId}")


            this.outputThread.start()
            this.inputThread.start()
        }
    }

    private fun close() {
        this.socket.close()
    }

}