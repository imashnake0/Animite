package com.imashnake.animite.social

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.imashnake.animite.core.ui.screen.FallbackScreen

@Composable
fun SocialScreen() {
    FallbackScreen(
        message = stringResource(R.string.coming_soon_social),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
}
