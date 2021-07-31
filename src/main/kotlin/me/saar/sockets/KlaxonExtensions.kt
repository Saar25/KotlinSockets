package me.saar.sockets

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import java.io.StringReader
import kotlin.reflect.KClass

fun <T : Any> Klaxon.parseFromClass(json: String, kClass: KClass<T>): T {
    val jsonObject = parser(kClass).parse(StringReader(json)) as JsonObject
    return fromJsonObject(jsonObject, kClass.java, kClass) as T
}