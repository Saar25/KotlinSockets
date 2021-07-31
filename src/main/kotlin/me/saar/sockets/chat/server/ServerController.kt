package me.saar.sockets.chat.server

import me.saar.sockets.IdProvider
import me.saar.sockets.MySocket
import me.saar.sockets.SocketService
import me.saar.sockets.chat.shared.ChatEnter
import me.saar.sockets.chat.shared.ChatLeave
import me.saar.sockets.chat.shared.ChatMessage
import me.saar.sockets.controller.Body
import me.saar.sockets.controller.Controller
import me.saar.sockets.controller.Endpoint
import me.saar.sockets.controller.Socket
import me.saar.sockets.reactive.Subscription

class ServerController : Controller {

    private val subscriptions = mutableMapOf<Int, Subscription>()

    companion object {
        private val idProvider = IdProvider()
    }

    @Endpoint
    fun join(@Socket client: MySocket) {
        val id = idProvider.next()
        client.send(id)

        val socketService = SocketService(client)
        this.subscriptions += (id to ChatStore.chatObservable.subscribe { e ->
            socketService.send(e.eventType, e)
        })

        ChatStore.clientEntered(ChatEnter(id))
    }

    @Endpoint
    fun message(@Body message: ChatMessage) {
        ChatStore.clientMessage(message)
    }

    @Endpoint
    fun exit(@Socket client: MySocket, @Body leave: ChatLeave) {
        client.close()

        this.subscriptions[leave.clientId]?.unsubscribe()

        ChatStore.clientLeft(leave)
    }
}