package com.imashnake.animite.core.extensions

/**
 * Adds two nullable lists together. If both lists are null, then null is returned.
 */
operator fun <T> List<T>?.plus(other: List<T>?): List<T>? {
    return if (this == null && other == null) {
        null
    } else if (this != null && other != null) {
        this + other
    } else this ?: other
}
