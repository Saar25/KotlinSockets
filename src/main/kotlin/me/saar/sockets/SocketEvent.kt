package me.saar.sockets

class SocketEvent(val endpoint: String, val body: String) {

    operator fun component1() = this.endpoint
    operator fun component2() = this.body

    companion object {
        fun parse(data: String): SocketEvent {
            val index = data.indexOf(' ')

            return if (index == -1) {
                SocketEvent(data, "")
            } else {
                val (endpoint, body) = data.splitAtIndex(index)

                SocketEvent(endpoint, body)
            }

        }
    }
}