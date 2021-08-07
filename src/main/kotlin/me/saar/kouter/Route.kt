package me.saar.kouter

internal data class Route<T>(
    val path: String,
    val handler: RouteHandler<T>,
)