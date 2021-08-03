package me.saar.sockets.chat.client

import me.saar.sockets.Client
import me.saar.sockets.chat.shared.ChatLeave
import me.saar.sockets.chat.shared.ChatMessage

class ClientService(private val client: Client, private val authService: AuthService) {

    fun join() {
        this.client.sendJson("join", null)
    }

    fun message(content: String) {
        val body = ChatMessage(this.authService.clientId, content)
        this.client.sendJson("message", body)
    }

    fun exit() {
        val body = ChatLeave(this.authService.clientId)
        this.client.sendJson("exit", body)
    }
}