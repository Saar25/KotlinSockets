package me.saar.sockets.chat.client

import me.saar.sockets.SocketService
import me.saar.sockets.chat.shared.ChatLeave
import me.saar.sockets.chat.shared.ChatMessage

class ClientService(private val socketService: SocketService, private val authService: AuthService) {

    fun join() {
        this.socketService.send("join", null)
    }

    fun message(content: String) {
        val body = ChatMessage(this.authService.clientId, content)
        this.socketService.send("message", body)
    }

    fun exit() {
        val body = ChatLeave(this.authService.clientId)
        this.socketService.send("exit", body)
    }
}