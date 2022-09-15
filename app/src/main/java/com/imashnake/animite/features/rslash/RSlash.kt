package com.imashnake.animite.features.rslash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination

// TODO: Add transitions as we did for `Profile`.
@Destination
@Composable
fun RSlash() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TODO: Unhardcode dimensions.
        LinearProgressIndicator(
            modifier = Modifier
                .clip(CircleShape)
                .width(100.dp)
                .height(3.dp),
            color = MaterialTheme.colorScheme.background,
            trackColor = MaterialTheme.colorScheme.primary
        )
    }
}
