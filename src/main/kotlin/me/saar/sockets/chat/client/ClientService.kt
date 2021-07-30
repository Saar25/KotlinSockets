package me.saar.sockets.chat.client

import me.saar.sockets.MySocket
import me.saar.sockets.SocketService
import me.saar.sockets.chat.shared.ChatLeave
import me.saar.sockets.chat.shared.ChatMessage

class ClientService(client: MySocket) {

    private val socketService = SocketService(client)

    fun join() = this.socketService.send("join", null)

    fun message(body: ChatMessage) = this.socketService.send("message", body)

    fun exit(body: ChatLeave) = this.socketService.send("exit", body)
}