package me.saar.sockets

fun interface SocketEventHandler {

    fun handle(input: SocketRouteInput)

}