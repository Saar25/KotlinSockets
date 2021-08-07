package me.saar.kouter.controller

import me.saar.kouter.RouteInput
import kotlin.reflect.KParameter

interface ArgProvider<T> {
    fun test(parameter: KParameter): Boolean
    fun provide(parameter: KParameter, input: RouteInput<T>): Any?
}