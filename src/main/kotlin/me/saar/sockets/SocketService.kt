package me.saar.sockets

import com.beust.klaxon.Klaxon

class SocketService(private val client: MySocket) {

    private val klaxon = Klaxon()

    fun send(endpoint: String, body: Any?) {
        val json = this.klaxon.toJsonString(body)
        this.client.send("$endpoint $json")
    }
}