package me.saar.sockets.chat.client

import me.saar.sockets.chat.shared.ChatEnter
import me.saar.sockets.chat.shared.ChatLeave
import me.saar.sockets.chat.shared.ChatMessage
import me.saar.sockets.controller.Body
import me.saar.sockets.controller.Controller
import me.saar.sockets.controller.Endpoint

class ClientController : Controller {

    @Endpoint
    fun enter(@Body enter: ChatEnter) = println(enter.toMessage())

    @Endpoint
    fun message(@Body message: ChatMessage) = println(message.toMessage())

    @Endpoint
    fun leave(@Body leave: ChatLeave) = println(leave.toMessage())

    @Endpoint
    fun shutdown() = Unit
}