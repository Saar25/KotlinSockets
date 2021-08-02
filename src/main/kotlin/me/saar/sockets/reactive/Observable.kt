package me.saar.sockets.reactive

interface Observable<T> {

    fun subscribe(onEvent: (T) -> Unit) = subscribe(Subscriber(onEvent))

    fun subscribe(onEvent: (T) -> Unit, onClose: () -> Unit) = subscribe(Subscriber(onEvent, onClose))

    fun subscribe(subscriber: Subscriber<T>): Subscription

}