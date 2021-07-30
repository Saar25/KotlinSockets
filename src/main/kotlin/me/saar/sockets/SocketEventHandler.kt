package me.saar.sockets

fun interface SocketEventHandler {

    fun handle(socket: MySocket, event: SocketEvent)

}