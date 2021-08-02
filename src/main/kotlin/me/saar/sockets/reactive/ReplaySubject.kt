package me.saar.sockets.reactive

class ReplaySubject<T> : Subject<T>() {

    private val values = mutableListOf<T>()

    override fun next(value: T) {
        this.values += value

        super.next(value)
    }

    override fun subscribe(subscriber: Subscriber<T>): Subscription {
        this.values.forEach(subscriber.onEvent)

        return super.subscribe(subscriber)
    }
}