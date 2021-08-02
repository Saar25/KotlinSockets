package me.saar.sockets.reactive

open class Subject<T> : Observable<T> {

    private val subscribers = mutableListOf<Subscriber<T>>()

    open fun next(value: T) {
        this.subscribers.forEach { it.onEvent(value) }
    }

    open fun close() {
        this.subscribers.forEach { it.onClose() }
    }

    override fun subscribe(subscriber: Subscriber<T>): Subscription {
        this.subscribers += subscriber

        return Subscription { this@Subject.subscribers.remove(subscriber) }
    }

}