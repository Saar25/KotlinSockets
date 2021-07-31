package me.saar.sockets.chat.shared

import me.saar.sockets.reactive.Observable
import me.saar.sockets.reactive.ReplaySubject
import me.saar.sockets.reactive.Subject

class ChatStore {

    private val chat: Subject<ChatEvent> = ReplaySubject()

    val chatObservable: Observable<ChatEvent> get() = chat

    fun serverMessage(chatServerMessage: ChatServerMessage) = chat.next(chatServerMessage)

    fun clientMessage(chatMessage: ChatMessage) = chat.next(chatMessage)

    fun clientEntered(chatEnter: ChatEnter) = chat.next(chatEnter)

    fun clientLeft(chatLeave: ChatLeave) = chat.next(chatLeave)

    fun shutdown() = chat.next(ChatShutdown)

}