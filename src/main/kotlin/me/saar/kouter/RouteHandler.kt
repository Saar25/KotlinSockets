package me.saar.kouter

fun interface RouteHandler<T> {
    fun handle(input: RouteInput<T>)
}