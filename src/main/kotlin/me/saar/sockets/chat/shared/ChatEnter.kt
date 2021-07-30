package me.saar.sockets.chat.shared

data class ChatEnter(val clientId: Int) : ChatEvent {

    override fun toMessage(): String = "Client $clientId has entered the chat"
}