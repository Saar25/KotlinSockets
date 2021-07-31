package me.saar.sockets.chat.shared

import com.beust.klaxon.Json
import me.saar.sockets.SocketEvent
import kotlin.reflect.KClass

interface ChatEvent {
    val endpoint: String
    fun toMessage(): String
}

data class ChatEnter(val clientId: Int) : ChatEvent {

    @Json(ignored = true)
    override val endpoint: String = "enter"

    override fun toMessage(): String = "Client $clientId has entered the chat"
}

data class ChatLeave(val clientId: Int) : ChatEvent {

    @Json(ignored = true)
    override val endpoint: String = "leave"

    override fun toMessage(): String = "Client $clientId has left the chat"
}

data class ChatMessage(val clientId: Int, val content: String) : ChatEvent {

    @Json(ignored = true)
    override val endpoint: String = "message"

    override fun toMessage(): String = "Client $clientId: $content"
}

data class ChatServerMessage(val content: String) : ChatEvent {

    @Json(ignored = true)
    override val endpoint: String = "server_message"

    override fun toMessage(): String = "Server: $content"
}

object ChatShutdown : ChatEvent {

    @Json(ignored = true)
    override val endpoint: String = "shutdown"

    override fun toMessage(): String = "Shutdown"
}

fun parseType(event: SocketEvent): KClass<out ChatEvent>? {
    return when (event.endpoint) {
        "enter" -> ChatEnter::class
        "leave" -> ChatLeave::class
        "message" -> ChatMessage::class
        "server_message" -> ChatServerMessage::class
        "shutdown" -> ChatShutdown::class
        else -> null
    }
}