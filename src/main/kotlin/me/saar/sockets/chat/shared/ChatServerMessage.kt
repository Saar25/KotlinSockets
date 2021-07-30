package me.saar.sockets.chat.shared

data class ChatServerMessage(val content: String) : ChatEvent {

    override fun toMessage(): String = "Server: $content"
}