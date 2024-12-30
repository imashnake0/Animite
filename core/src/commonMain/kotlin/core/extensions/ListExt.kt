package core.extensions

/**
 * Adds two nullable lists together. If both lists are null, then null is returned.
 */
operator fun <T> List<T>?.plus(other: List<T>?): List<T>? {
    val newList = this?.let(::ArrayList)
    newList?.addAll(other.orEmpty())
    return newList ?: other
}
