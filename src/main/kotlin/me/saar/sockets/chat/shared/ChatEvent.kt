package me.saar.sockets.chat.shared

interface ChatEvent {

    fun toMessage(): String

}