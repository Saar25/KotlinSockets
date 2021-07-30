package me.saar.sockets.chat.shared

data class ChatLeave(val clientId: Int) : ChatEvent {

    override fun toMessage(): String = "Client $clientId has left the chat"
}