package me.saar.kouter


fun pathParts(path: String) = path.let { if (it.startsWith("/")) it.subSequence(1, it.length) else it }.split("/")

internal fun Route<*>.match(path: String): Boolean {
    val thisParts = pathParts(this.path)
    val otherParts = pathParts(path)
    return thisParts == otherParts
}