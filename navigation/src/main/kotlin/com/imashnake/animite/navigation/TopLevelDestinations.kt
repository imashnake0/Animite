package com.imashnake.animite.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileRoute(
    @SerialName("accessToken")
    val accessToken: String? = null,
    @SerialName("tokenType")
    val tokenType: String? = null,
    @SerialName("expiresIn")
    val expiresIn: Int = -1
)

@Serializable
data object SocialRoute

@Serializable
data object HomeRoute
