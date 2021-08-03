package me.saar.sockets

import me.saar.sockets.reactive.ReplaySubject
import java.net.SocketException
import kotlin.concurrent.thread

inline fun whileNotClosed(closeable: CloseableState, callback: () -> Unit) {
    try {
        while (!closeable.isClosed) {
            callback.invoke()
        }
    } catch (e: SocketException) {
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        closeable.close()
    }
}

fun socketEventThread(client: Client, onEvent: (SocketEvent) -> Unit, onClose: () -> Unit) = thread {
    whileNotClosed(client) {
        client.readEvent()?.let { event ->
            onEvent.invoke(event)
        }
    }
    onClose.invoke()
}

fun socketEventSubject(client: Client): ReplaySubject<SocketEvent> {
    val subject = ReplaySubject<SocketEvent>()

    socketEventThread(client, subject::next, subject::close)

    return subject
}