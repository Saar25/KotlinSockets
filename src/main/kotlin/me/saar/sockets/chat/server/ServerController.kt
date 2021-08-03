package me.saar.sockets.chat.server

import me.saar.sockets.Client
import me.saar.sockets.IdProvider
import me.saar.sockets.chat.shared.*
import me.saar.sockets.controller.Body
import me.saar.sockets.controller.Controller
import me.saar.sockets.controller.Endpoint
import me.saar.sockets.controller.Socket
import me.saar.sockets.reactive.Subscription

class ServerController(private val chatStore: ChatStore) : Controller {

    private val subscriptions = mutableMapOf<Int, Subscription>()

    companion object {
        private val idProvider = IdProvider()
    }

    @Endpoint
    fun join(@Socket client: Client) {
        val id = idProvider.next()
        client.sendJson("verify", ChatVerify(id))

        this.subscriptions += (id to this.chatStore.chatObservable.subscribe { e ->
            client.sendJson(e.eventType, e)
        })

        this.chatStore.clientEntered(ChatEnter(id))
    }

    @Endpoint
    fun message(@Body message: ChatMessage) {
        this.chatStore.clientMessage(message)
    }

    @Endpoint
    fun exit(@Socket client: Client, @Body leave: ChatLeave) {
        client.close()

        this.subscriptions[leave.clientId]?.unsubscribe()

        this.chatStore.clientLeft(leave)
    }
}