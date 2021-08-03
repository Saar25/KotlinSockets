package me.saar.sockets

class SocketRouter : SocketEventHandler {

    private data class SocketRoute(val endpoint: String, val callback: SocketEventHandler)

    private val routes = mutableListOf<SocketRoute>()

    fun route(endpoint: String, callback: SocketEventHandler) {
        this.routes += SocketRoute(endpoint, callback)
    }

    override fun handle(input: SocketRouteInput) {
        val route = this.routes.find { it.endpoint == input.event.endpoint }
        route?.also { it.callback.handle(input) }
    }

}
