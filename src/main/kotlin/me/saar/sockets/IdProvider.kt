package me.saar.sockets

class IdProvider {

    private var id = 0

    fun next() = this.id++

}