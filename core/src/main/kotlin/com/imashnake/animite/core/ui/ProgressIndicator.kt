package com.imashnake.animite.core.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator() {
    LinearProgressIndicator(
        modifier = Modifier
            .clip(CircleShape)
            .width(100.dp)
            .height(3.dp),
        color = MaterialTheme.colorScheme.background,
        trackColor = MaterialTheme.colorScheme.primary
    )
}
