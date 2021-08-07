package me.saar.sockets.chat.client

import me.saar.sockets.Client
import me.saar.sockets.ConsoleEvent
import me.saar.sockets.controller.Body
import me.saar.sockets.controller.Controller
import me.saar.sockets.controller.Endpoint

class ClientConsoleController(private val client: Client, private val clientService: ClientService) : Controller {

    @Endpoint
    fun send(event: ConsoleEvent) {
        this.clientService.message(event.body)
    }

    @Endpoint
    fun exit() {
        this.clientService.exit()
        this.client.close()
    }
}