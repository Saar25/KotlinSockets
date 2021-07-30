package me.saar.sockets.chat.server

import me.saar.sockets.chat.shared.ChatEnter
import me.saar.sockets.chat.shared.ChatEvent
import me.saar.sockets.chat.shared.ChatLeave
import me.saar.sockets.chat.shared.ChatMessage
import me.saar.sockets.reactive.Observable
import me.saar.sockets.reactive.ReplaySubject
import me.saar.sockets.reactive.Subject

object ChatStore {

    private val chat: Subject<ChatEvent> = ReplaySubject()

    val chatObservable: Observable<ChatEvent> get() = this.chat

    fun clientMessage(chatMessage: ChatMessage) = this.chat.next(chatMessage)

    fun clientEntered(chatEnter: ChatEnter) = this.chat.next(chatEnter)

    fun clientLeft(chatLeave: ChatLeave) = this.chat.next(chatLeave)

}