package com.imashnake.animite.features.rslash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imashnake.animite.features.theme.AnimiteTheme
import com.imashnake.animite.features.theme.Backdrop
import com.imashnake.animite.features.theme.Text
import com.ramcosta.composedestinations.annotation.Destination

// TODO: Add transitions as we did for `Profile`.
@Destination
@Composable
fun RSlash() {
    AnimiteTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onBackground,
                strokeWidth = 8.dp
            )
        }
    }
}
