package me.saar.sockets.reactive

class BehaviorSubject<T>(private var value: T) : Subject<T>() {

    override fun next(value: T) {
        this.value = value

        super.next(value)
    }

    override fun subscribe(subscriber: Subscriber<T>): Subscription {
        subscriber.onEvent(this.value)

        return super.subscribe(subscriber)
    }
}