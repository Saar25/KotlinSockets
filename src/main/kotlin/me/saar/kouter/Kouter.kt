package me.saar.kouter

fun pathParts(path: String) = path
    .let { if (!it.startsWith("/")) "/$it" else it }
    .let { if (it.endsWith("/")) it.substring(0, it.length - 1) else it }
    .split("/")

fun localPath(original: String, local: String): String {
    val originalParts = pathParts(original)
    val localParts = pathParts(local)

    return originalParts.subList(localParts.size, originalParts.size).joinToString("/")
}

internal fun Route<*>.match(path: String): Boolean {
    val thisParts = pathParts(this.path)
    val otherParts = pathParts(path)

    if (thisParts.size > otherParts.size) return false

    return thisParts.withIndex().all { it.value == otherParts[it.index] }
}