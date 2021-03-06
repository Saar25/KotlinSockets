package me.saar.sockets.chat.shared

import me.saar.sockets.reactive.Observable
import me.saar.sockets.reactive.ReplaySubject
import me.saar.sockets.reactive.Subject

class ChatStore {

    private val chat: Subject<ChatEvent> = ReplaySubject()

    val chatObservable: Observable<ChatEvent> get() = this.chat

    fun serverMessage(chatServerMessage: ChatServerMessage) = this.chat.next(chatServerMessage)

    fun clientMessage(chatMessage: ChatMessage) = this.chat.next(chatMessage)

    fun clientEntered(chatEnter: ChatEnter) = this.chat.next(chatEnter)

    fun clientVerified(chatVerify: ChatVerify) = this.chat.next(chatVerify)

    fun clientLeft(chatLeave: ChatLeave) = this.chat.next(chatLeave)

    fun shutdown() = this.chat.next(ChatShutdown)

}