package me.saar.sockets.chat.client

import com.github.saar25.kouter.controller.Controller
import com.github.saar25.kouter.controller.Data
import com.github.saar25.kouter.controller.Endpoint
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