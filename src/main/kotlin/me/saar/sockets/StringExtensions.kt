package me.saar.sockets

fun String.splitAtIndex(index: Int): Pair<String, String> {
    return Pair(substring(0, index), substring(index + 1))
}