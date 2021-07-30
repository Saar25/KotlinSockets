package me.saar.sockets.reactive

interface Observable<T> {

    fun subscribe(subscriber: Subscriber<T>): Subscription

}