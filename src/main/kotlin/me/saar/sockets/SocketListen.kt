package me.saar.sockets

import java.net.SocketException

inline fun socketListen(client: MySocket, crossinline eventEmitter: (SocketEvent) -> Unit) {
    try {
        while (!client.isClosed) {
            client.read()?.let {
                val event = SocketEvent.parse(it)

                eventEmitter(event)
            }
        }
    } catch (e: SocketException) {
    } catch (e: Exception) {
        e.printStackTrace()
    }
}