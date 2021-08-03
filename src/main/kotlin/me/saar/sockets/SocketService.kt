package me.saar.sockets

import com.beust.klaxon.Klaxon

class SocketService(private val client: Client) {

    private val klaxon = Klaxon()

    fun send(endpoint: String, body: Any?) {
        val json = this.klaxon.toJsonString(body)
        val event = SocketEvent(endpoint, json)
        this.client.sendEvent(event)
    }
}