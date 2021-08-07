package me.saar.kouter

data class RouteInput<T>(
    val path: String,
    val data: T,
)