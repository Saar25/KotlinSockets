package me.saar.sockets.chat.shared

object ChatShutdown : ChatEvent {

    override fun toMessage(): String = "Shutdown"
}