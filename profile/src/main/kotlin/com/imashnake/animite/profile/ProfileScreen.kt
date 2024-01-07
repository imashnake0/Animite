package com.imashnake.animite.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_DEEPLINK
import com.imashnake.animite.profile.dev.internal.ANILIST_AUTH_URL
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@Destination(
    route = "user",
    deepLinks = [
        DeepLink(
            uriPattern = ANILIST_AUTH_DEEPLINK
        )
    ]
)
@RootNavGraph(start = true)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    accessToken: String? = null
) {
    accessToken?.let { viewModel.setAccessToken(it) }
    val actualAccessToken by viewModel.accessToken.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val uriHandler = LocalUriHandler.current

        if (actualAccessToken == null) {
            Button(onClick = { uriHandler.openUri(ANILIST_AUTH_URL) }) {
                Text(text = "Log in")
            }
        } else {
            Text(text = "Saved access token!")
        }
    }
}
