package com.imashnake.animite.navigation

/**
 * Builds a key via [toString] for shared element transitions from [Component] to [Component].
 *
 * @see [androidx.compose.animation.SharedTransitionScope.rememberSharedContentState]
 * @see [androidx.compose.animation.SharedTransitionScope.SharedContentState.key]
 */
data class SharedContentKey(
    val id: Int? = null,
    val source: String? = null,
    val sharedComponents: Pair<Component, Component>
) {
    enum class Component {
        Card,
        Page,
        Text,
        Image,
    }

    override fun toString() = "${sharedComponents.first}_${sharedComponents.second}_${id}_${source}"
}
