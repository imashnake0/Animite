package com.imashnake.animite.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import animite.core.generated.resources.Res
import animite.core.generated.resources.coming_soon
import org.jetbrains.compose.resources.stringResource

@Composable
fun ComingSoonScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                LocalPaddings.current.tiny
            )
        ) {
            ComingSoonMessage()
        }
    }
}

@Composable
fun ComingSoonMessage(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(Res.string.coming_soon),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
    )
}