package me.saar.sockets

interface CloseableState : AutoCloseable {
    val isClosed: Boolean

    override fun close()
}