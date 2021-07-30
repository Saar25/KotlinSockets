package me.saar.sockets

class SocketRouter : SocketEventHandler {

    private class SocketRoute(val endpoint: String, val callback: SocketEventHandler) {
        operator fun component1() = this.endpoint
        operator fun component2() = this.callback
    }

    private val routes = mutableListOf<SocketRoute>()

    fun route(endpoint: String, callback: SocketEventHandler) {
        this.routes += SocketRoute(endpoint, callback)
    }

    override fun handle(socket: MySocket, event: SocketEvent) {
        val route = this.routes.find { it.endpoint == event.endpoint }
        route?.also { it.callback.handle(socket, event) }
    }

}
