package me.saar.sockets

data class SocketEvent(val endpoint: String, val body: String) {

    override fun toString() = "$endpoint $body"

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