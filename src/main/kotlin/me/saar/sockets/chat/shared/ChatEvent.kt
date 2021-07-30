package me.saar.sockets.chat.shared

interface ChatEvent {
    fun toEvent(): String
    fun toMessage(): String
}

data class ChatEnter(val clientId: Int) : ChatEvent {
    override fun toEvent(): String = "enter $clientId"
    override fun toMessage(): String = "Client $clientId has entered the chat"
}

data class ChatLeave(val clientId: Int) : ChatEvent {
    override fun toEvent(): String = "leave $clientId"
    override fun toMessage(): String = "Client $clientId has left the chat"
}

data class ChatMessage(val clientId: Int, val content: String) : ChatEvent {
    override fun toEvent(): String = "message $clientId $content"
    override fun toMessage(): String = "Client $clientId: $content"
}

data class ChatServerMessage(val content: String) : ChatEvent {
    override fun toEvent(): String = "message server $content"
    override fun toMessage(): String = "Server: $content"
}

object ChatShutdown : ChatEvent {
    override fun toEvent(): String = "shutdown"
    override fun toMessage(): String = "Shutdown"
}