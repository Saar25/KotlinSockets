package me.saar.sockets

typealias Callback = (MySocket, String) -> Unit

class SocketRouter {

    private class SocketRoute(val endpoint: String, val callback: Callback) {
        operator fun component1() = this.endpoint
        operator fun component2() = this.callback
    }

    private val routes = mutableListOf<SocketRoute>()

    fun route(endpoint: String, callback: Callback) {
        this.routes += SocketRoute(endpoint, callback)
    }

    fun onEvent(socket: MySocket, event: SocketEvent<String>) {
        val route = this.routes.find { it.endpoint == event.endpoint }
        route?.also { it.callback(socket, event.body) }
    }

}
