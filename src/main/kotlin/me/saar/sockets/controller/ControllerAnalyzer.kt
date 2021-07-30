package me.saar.sockets.controller

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import me.saar.sockets.MySocket
import me.saar.sockets.SocketRouter
import java.io.StringReader
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmErasure

private val klaxon = Klaxon()

private fun Klaxon.parse(json: String, kClass: KClass<out Any>): Any {
    val jsonObject = parser(kClass).parse(StringReader(json)) as JsonObject
    return fromJsonObject(jsonObject, kClass.java, kClass)
}

private class ControllerEndpoint(val endpoint: String, val function: KFunction<*>) {
    operator fun component1() = this.endpoint
    operator fun component2() = this.function
}

private fun findControllerEndpoints(controller: KClass<out Controller>): List<ControllerEndpoint> {
    return controller.memberFunctions.filter { it.annotations.any { a -> a is Endpoint } }.map { f ->
        val annotation = f.annotations.find { a -> a is Endpoint } as Endpoint
        val endpoint = annotation.endpoint.ifEmpty { f.name }
        return@map ControllerEndpoint(endpoint, f)
    }
}

private fun parameters(function: KFunction<*>, controller: Controller, socket: MySocket, data: String) =
    function.parameters.map { p ->
        when {
            p.index == 0 -> controller
            p.annotations.any { a -> a is Socket } -> socket
            p.annotations.any { a -> a is Body } -> klaxon.parse(data, p.type.jvmErasure)
            else -> null
        }
    }

fun Controller.buildRouter() = SocketRouter().apply {
    val controllerEndpoints = findControllerEndpoints(this@buildRouter::class)

    for ((endpoint, function) in controllerEndpoints) {
        route(endpoint) { socket, event ->
            val parameters = parameters(function, this@buildRouter, socket, event.body)

            function.call(*parameters.toTypedArray())
        }
    }
}
