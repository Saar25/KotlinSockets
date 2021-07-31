package me.saar.sockets

class SocketEvent(val endpoint: String, val body: String) {

    operator fun component1() = this.endpoint
    operator fun component2() = this.body

    override fun toString() = "$endpoint $body"

    companion object {
        private fun String.splitAtIndex(index: Int): Pair<String, String> {
            return Pair(substring(0, index), substring(index + 1))
        }

        fun parse(data: String): SocketEvent {
            val separate = data.indexOf(' ')

            val (endpoint, body) = data.splitAtIndex(separate)

            return SocketEvent(endpoint, body)
        }
    }
}