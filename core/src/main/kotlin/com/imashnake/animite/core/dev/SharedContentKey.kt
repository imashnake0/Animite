package com.imashnake.animite.core.dev

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
