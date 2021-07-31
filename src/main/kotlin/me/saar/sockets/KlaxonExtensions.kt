package me.saar.sockets

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import java.io.StringReader
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

fun <T : Any> Klaxon.parseFromClass(json: String, kClass: KClass<T>): T {
    val jsonObject = parser(kClass).parse(StringReader(json)) as JsonObject

    val cast = kClass.safeCast(jsonObject)

    return cast ?: fromJsonObject(jsonObject, kClass.java, kClass) as T
}

fun <T : Any> Klaxon.parseFieldFromClass(json: String, field: String, kClass: KClass<T>): T? {
    val jsonObject = parseJsonObject(StringReader(json))

    val cast = kClass.safeCast(jsonObject[field])

    return cast ?: jsonObject.obj(field)?.let { fromJsonObject(it, kClass.java, kClass) as T? }
}