package me.saar.sockets.reactive

data class Subscriber<T>(
    val onEvent: (T) -> Unit = {},
    val onClose: () -> Unit = {},
)