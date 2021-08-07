package me.saar.sockets.controller

import com.beust.klaxon.Klaxon
import me.saar.kouter.RouteInput
import me.saar.kouter.controller.ArgProvider
import me.saar.sockets.Client
import me.saar.sockets.SocketRouteInput
import me.saar.sockets.parseFieldFromClass
import me.saar.sockets.parseFromClass
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Body

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class BodyField(val name: String = "")

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Socket

private val klaxon = Klaxon()

private val socketArgProvider = object : ArgProvider<SocketRouteInput> {
    override fun test(parameter: KParameter): Boolean {
        return parameter.annotations.any { it is Socket }
    }

    override fun provide(parameter: KParameter, input: RouteInput<SocketRouteInput>): Client {
        return input.data.client
    }
}

private val bodyArgProvider = object : ArgProvider<SocketRouteInput> {
    override fun test(parameter: KParameter): Boolean {
        return parameter.annotations.any { it is Body }
    }

    override fun provide(parameter: KParameter, input: RouteInput<SocketRouteInput>): Any {
        return klaxon.parseFromClass(input.data.event.body, parameter.type.jvmErasure)
    }
}

private val bodyFieldArgProvider = object : ArgProvider<SocketRouteInput> {
    override fun test(parameter: KParameter): Boolean {
        return parameter.annotations.any { it is BodyField }
    }

    override fun provide(parameter: KParameter, input: RouteInput<SocketRouteInput>): Any? {
        val annotation = parameter.annotations.find { it is BodyField } as BodyField
        val field = annotation.name.ifEmpty { parameter.name!! }

        return klaxon.parseFieldFromClass(input.data.event.body, field, parameter.type.jvmErasure)
    }
}

val socketArgProviders = listOf(socketArgProvider, bodyArgProvider, bodyFieldArgProvider)