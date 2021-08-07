package me.saar.sockets.chat.client

import me.saar.sockets.chat.shared.*
import me.saar.sockets.controller.Body
import me.saar.sockets.controller.Controller
import me.saar.sockets.controller.Endpoint

class ClientSocketController(private val chatStore: ChatStore, private val authService: AuthService) : Controller {

    @Endpoint
    fun verify(@Body verify: ChatVerify) {
        this.chatStore.clientVerified(verify)
        this.authService.clientId = verify.clientId
    }

    @Endpoint
    fun enter(@Body enter: ChatEnter) = this.chatStore.clientEntered(enter)

    @Endpoint
    fun message(@Body message: ChatMessage) = this.chatStore.clientMessage(message)

    @Endpoint("server_message")
    fun serverMessage(@Body serverMessage: ChatServerMessage) = this.chatStore.serverMessage(serverMessage)

    @Endpoint
    fun leave(@Body leave: ChatLeave) = this.chatStore.clientLeft(leave)

    @Endpoint
    fun shutdown() = this.chatStore.shutdown()
}