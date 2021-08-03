package me.saar.sockets.controller

import com.beust.klaxon.Klaxon
import me.saar.sockets.Client
import me.saar.sockets.SocketRouteInput
import me.saar.sockets.parseFieldFromClass
import me.saar.sockets.parseFromClass
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

interface EndpointParamParser {
    fun test(parameter: KParameter): Boolean
    fun parse(parameter: KParameter, input: SocketRouteInput): Any?
}

private val klaxon = Klaxon()

val socketParamParser = object : EndpointParamParser {
    override fun test(parameter: KParameter): Boolean {
        return parameter.annotations.any { it is Socket }
    }

    override fun parse(parameter: KParameter, input: SocketRouteInput): Client {
        return input.client
    }
}

val bodyParamParser = object : EndpointParamParser {
    override fun test(parameter: KParameter): Boolean {
        return parameter.annotations.any { it is Body }
    }

    override fun parse(parameter: KParameter, input: SocketRouteInput): Any {
        return klaxon.parseFromClass(input.event.body, parameter.type.jvmErasure)
    }
}

val bodyFieldParamParser = object : EndpointParamParser {
    override fun test(parameter: KParameter): Boolean {
        return parameter.annotations.any { it is BodyField }
    }

    override fun parse(parameter: KParameter, input: SocketRouteInput): Any? {
        val annotation = parameter.annotations.find { it is BodyField } as BodyField
        val field = annotation.name.ifEmpty { parameter.name!! }

        return klaxon.parseFieldFromClass(input.event.body, field, parameter.type.jvmErasure)
    }
}

val unknownParamParser = object : EndpointParamParser {
    override fun test(parameter: KParameter): Boolean = true

    override fun parse(parameter: KParameter, input: SocketRouteInput): Any? = null
}

val parsers = listOf(socketParamParser, bodyParamParser, bodyFieldParamParser)

fun findParser(parameter: KParameter): EndpointParamParser =
    parsers.find { it.test(parameter) } ?: unknownParamParser