package me.saar.sockets

import kotlin.concurrent.thread

fun socketListen(client: MySocket, eventEmitter: (SocketEvent<String>) -> Unit) = thread {
    while (!client.isClosed) {
        client.read()?.let {
            val event = SocketEvent.parse(it)

            eventEmitter(event)
        }
    }
}
