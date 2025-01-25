package com.imashnake.animite.social

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.imashnake.animite.core.ui.FallbackScreen
import com.imashnake.animite.core.R as coreR

@Composable
fun SocialScreen() {
    FallbackScreen(
        message = stringResource(coreR.string.coming_soon),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
}
