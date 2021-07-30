package me.saar.sockets

import java.net.SocketException
import kotlin.concurrent.thread

fun socketListen(client: MySocket, eventEmitter: (SocketEvent) -> Unit) = thread {
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