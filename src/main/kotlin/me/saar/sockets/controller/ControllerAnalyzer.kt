package me.saar.sockets.controller

import me.saar.kouter.Router
import me.saar.sockets.SocketRouteInput
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions

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

private fun parameters(function: KFunction<*>, controller: Controller): List<(SocketRouteInput) -> Any?> =
    function.parameters.map { p ->
        if (p.index == 0) {
            return@map { controller }
        } else {
            val parser = findParser(p)

            return@map { input: SocketRouteInput -> parser.parse(p, input) }
        }
    }

fun Controller.buildRouter() = Router<SocketRouteInput>().apply {
    val controllerEndpoints = findControllerEndpoints(this@buildRouter::class)

    for ((endpoint, function) in controllerEndpoints) {
        val parameters = parameters(function, this@buildRouter)

        route(endpoint) { input ->
            function.call(*parameters.map { it(input.data) }.toTypedArray())
        }
    }
}
