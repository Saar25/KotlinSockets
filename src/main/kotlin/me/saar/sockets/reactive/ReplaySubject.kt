package me.saar.sockets.reactive

class ReplaySubject<T> : Subject<T>() {

    private val values = mutableListOf<T>()

    override fun next(value: T) {
        super.next(value)

        this.values += value
    }

    override fun subscribe(subscriber: Subscriber<T>): Subscription {
        this.values.forEach(subscriber)

        return super.subscribe(subscriber)
    }
}