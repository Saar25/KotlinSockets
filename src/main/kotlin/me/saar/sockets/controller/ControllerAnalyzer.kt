package me.saar.sockets.controller

import com.beust.klaxon.Klaxon
import me.saar.sockets.MySocket
import me.saar.sockets.SocketRouter
import me.saar.sockets.parseFieldFromClass
import me.saar.sockets.parseFromClass
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmErasure

private val klaxon = Klaxon()

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

private fun parameters(function: KFunction<*>, controller: Controller): List<(MySocket, String) -> Any?> =
    function.parameters.map { p ->
        when {
            p.index == 0 -> {
                return@map { _, _ -> controller }
            }
            p.annotations.any { a -> a is Socket } -> {
                return@map { socket, _ -> socket }
            }
            p.annotations.any { a -> a is Body } -> {
                val kClass = p.type.jvmErasure

                return@map { _, data -> klaxon.parseFromClass(data, kClass) }
            }
            p.annotations.any { a -> a is BodyField } -> {
                val kClass = p.type.jvmErasure
                val annotation = p.annotations.find { a -> a is BodyField } as BodyField
                val bodyField = annotation.name.ifEmpty { p.name!! }

                return@map { _, data -> klaxon.parseFieldFromClass(data, bodyField, kClass) }
            }
            else -> { _: MySocket, _: String -> null }
        }
    }

fun Controller.buildRouter() = SocketRouter().apply {
    val controllerEndpoints = findControllerEndpoints(this@buildRouter::class)

    for ((endpoint, function) in controllerEndpoints) {
        val parameters = parameters(function, this@buildRouter)

        route(endpoint) { socket, event ->
            function.call(*parameters.map { it(socket, event.body) }.toTypedArray())
        }
    }
}
