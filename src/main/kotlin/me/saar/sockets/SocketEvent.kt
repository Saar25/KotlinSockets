package me.saar.sockets

class SocketEvent(val endpoint: String, val body: String) {

    operator fun component1() = this.endpoint
    operator fun component2() = this.body

    companion object {
        fun parse(data: String): SocketEvent {
            val separate = data.indexOf(' ')

            val (endpoint, body) = data.splitAtIndex(separate)

            return SocketEvent(endpoint, body)
        }
    }
}