package me.saar.sockets

import me.saar.sockets.reactive.ReplaySubject
import java.net.ServerSocket
import java.net.SocketException
import kotlin.concurrent.thread

inline fun whileSocketIsOpen(socket: ServerSocket, callback: () -> Unit) {
    try {
        while (!socket.isClosed) {
            callback.invoke()
        }
    } catch (e: SocketException) {
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        socket.close()
    }
}

inline fun whileSocketIsOpen(socket: MySocket, callback: () -> Unit) {
    try {
        while (!socket.isClosed) {
            callback.invoke()
        }
    } catch (e: SocketException) {
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        socket.close()
    }
}

fun socketEventThread(socket: MySocket, onEvent: (SocketEvent) -> Unit, onClose: () -> Unit) = thread {
    whileSocketIsOpen(socket) {
        socket.read()?.let {
            val event = SocketEvent.parse(it)

            onEvent.invoke(event)
        }
    }
    onClose.invoke()
}

fun socketEventSubject(client: MySocket): ReplaySubject<SocketEvent> {
    val subject = ReplaySubject<SocketEvent>()

    socketEventThread(client, subject::next, subject::close)

    return subject
}