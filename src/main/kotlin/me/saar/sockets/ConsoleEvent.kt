package me.saar.sockets

data class ConsoleEvent(val endpoint: String, val body: String) {

    override fun toString() = "$endpoint $body"

    companion object {
        fun parse(data: String): ConsoleEvent {
            val index = data.indexOf(' ')

            return if (index == -1) {
                ConsoleEvent(data, "")
            } else {
                val (endpoint, body) = data.splitAtIndex(index)

                ConsoleEvent(endpoint, body)
            }

        }
    }
}