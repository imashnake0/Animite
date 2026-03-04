package com.imashnake.animite.navigation

import android.content.Intent
import androidx.core.net.toUri
import androidx.navigation3.runtime.NavKey
import javax.inject.Inject

class DeeplinkHandler @Inject constructor() {

    companion object {
        private const val REDIRECT_SCHEME = "app"
        private const val REDIRECT_HOST = "animite"

        private const val ACCESS_TOKEN = "access_token"
        private const val TOKEN_TYPE = "token_type"
        private const val EXPIRES_IN = "expires_in"
    }

    fun parseIntent(intent: Intent?): NavKey? {
        val data = intent?.data ?: return null

        // Query params are currently encoded as a Fragment, and that doesn't play nice with the URI
        val normalised = intent.dataString?.replace("#", "?")?.toUri()

        val scheme = data.scheme == REDIRECT_SCHEME
        val host = data.host == REDIRECT_HOST
        val queryParams = normalised
            ?.queryParameterNames
            ?.associateWith { param -> normalised.getQueryParameter(param) }
            .orEmpty()

        if (scheme && host && queryParams.isNotEmpty()) {
            return ProfileRoute(
                accessToken = queryParams[ACCESS_TOKEN],
                tokenType = queryParams[TOKEN_TYPE],
                expiresIn = queryParams[EXPIRES_IN]?.toIntOrNull() ?: -1
            )
        }

        return null
    }
}
