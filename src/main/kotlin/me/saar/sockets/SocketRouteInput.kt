package me.saar.sockets

class SocketRouteInput(val socket: MySocket, val event: SocketEvent) {
    operator fun component1() = this.socket
    operator fun component2() = this.event

}