package me.saar.sockets.chat.client

import me.saar.kouter.controller.Controller
import me.saar.kouter.controller.Data
import me.saar.kouter.controller.Endpoint
import me.saar.sockets.Client
import me.saar.sockets.ConsoleEvent

class ClientConsoleController(private val client: Client, private val clientService: ClientService) : Controller {

    @Endpoint
    fun send(@Data event: ConsoleEvent) {
        this.clientService.message(event.body)
    }

    @Endpoint
    fun exit() {
        this.clientService.exit()
        this.client.close()
    }
}