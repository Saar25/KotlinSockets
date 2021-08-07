package me.saar.sockets

import me.saar.kouter.RouteInput
import me.saar.kouter.Router

class ConsoleApp(private val consoleRouter: Router<ConsoleEvent>) {

    fun start(exitCode: String) {
        consoleEventSubject(exitCode).subscribe(
            onEvent = {
                val input = RouteInput(it.endpoint, it)
                this.consoleRouter.handle(input)
            },
            onClose = {
                println("ConsoleApp Goodbye")
            }
        )
    }
}