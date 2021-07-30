package me.saar.sockets.chat.shared

data class ChatMessage(val clientId: Int, val content: String) : ChatEvent {

    override fun toMessage(): String = "Client $clientId: $content"
}