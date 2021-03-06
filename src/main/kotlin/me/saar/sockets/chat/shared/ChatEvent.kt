package me.saar.sockets.chat.shared

import com.beust.klaxon.Json
import me.saar.sockets.SocketEvent
import kotlin.reflect.KClass

interface ChatEvent {
    val eventType: String
    fun toMessage(): String
}

data class ChatVerify(val clientId: Int) : ChatEvent {

    @Json(ignored = true)
    override val eventType: String = "verify"

    override fun toMessage(): String = "Server granted you with id $clientId"
}

data class ChatEnter(val clientId: Int) : ChatEvent {

    @Json(ignored = true)
    override val eventType: String = "enter"

    override fun toMessage(): String = "Client $clientId has entered the chat"
}

data class ChatLeave(val clientId: Int) : ChatEvent {

    @Json(ignored = true)
    override val eventType: String = "leave"

    override fun toMessage(): String = "Client $clientId has left the chat"
}

data class ChatMessage(val clientId: Int, val content: String) : ChatEvent {

    @Json(ignored = true)
    override val eventType: String = "message"

    override fun toMessage(): String = "Client $clientId: $content"
}

data class ChatServerMessage(val content: String) : ChatEvent {

    @Json(ignored = true)
    override val eventType: String = "server_message"

    override fun toMessage(): String = "Server: $content"
}

object ChatShutdown : ChatEvent {

    @Json(ignored = true)
    override val eventType: String = "shutdown"

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