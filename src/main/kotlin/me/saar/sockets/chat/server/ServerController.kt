package me.saar.sockets.chat.server

import me.saar.sockets.IdProvider
import me.saar.sockets.MySocket
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

    @Endpoint("join")
    fun join(@Socket client: MySocket) {
        val id = idProvider.next()
        client.send(id)

        this.subscriptions += (id to ChatStore.chatObservable.subscribe { e ->
            client.send(e.toMessage())
        })

        ChatStore.clientEntered(ChatEnter(id))
    }

    @Endpoint("message")
    fun message(@Body message: ChatMessage) {
        ChatStore.clientMessage(message)
    }

    @Endpoint("exit")
    fun exit(@Socket client: MySocket, @Body leave: ChatLeave) {
        client.close()

        this.subscriptions[leave.clientId]?.unsubscribe()

        ChatStore.clientLeft(leave)
    }
}