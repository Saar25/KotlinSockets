package me.saar.kouter.controller

import me.saar.kouter.RouteInput
import me.saar.kouter.Router
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions

private data class ControllerEndpoint(val endpoint: String, val function: KFunction<*>)

private fun findControllerEndpoints(controller: KClass<out Controller>): List<ControllerEndpoint> {
    return controller.memberFunctions.filter { it.annotations.any { a -> a is Endpoint } }.map { f ->
        val annotation = f.annotations.find { a -> a is Endpoint } as Endpoint
        val endpoint = annotation.endpoint.ifEmpty { f.name }
        return@map ControllerEndpoint(endpoint, f)
    }
}

private fun <T> List<ArgProvider<T>>.match(f: KFunction<*>, controller: Controller): List<(RouteInput<T>) -> Any?> =
    f.parameters.map { p ->
        if (p.index == 0) {
            return@map { controller }
        }

        find { it.test(p) }?.let { provider ->
            return@map { input -> provider.provide(p, input) }
        }

        if (p.annotations.any { it is Path }) {
            return@map { it.path }
        }

        if (p.annotations.any { it is Data }) {
            return@map { it.data }
        }

        return@map { null }
    }

fun <T> Controller.buildRouter(providers: List<ArgProvider<T>> = emptyList()): Router<T> = Router<T>().apply {
    val endpoints = findControllerEndpoints(this@buildRouter::class)

    for ((endpoint, function) in endpoints) {
        val matchedProviders = providers.match(function, this@buildRouter)

        route(endpoint) { input ->
            val arguments = matchedProviders.map { it(input) }
            function.call(*arguments.toTypedArray())
        }
    }
}