package me.saar.sockets.reactive

class BehaviorSubject<T>(private var value: T) : Subject<T>() {

    override fun next(value: T) {
        super.next(value)

        this.value = value
    }

    override fun subscribe(subscriber: Subscriber<T>): Subscription {
        subscriber(this.value)

        return super.subscribe(subscriber)
    }
}