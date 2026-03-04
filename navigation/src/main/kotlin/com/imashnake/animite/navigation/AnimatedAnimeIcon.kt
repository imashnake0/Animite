package com.imashnake.animite.navigation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource

@Composable
internal fun AnimatedAnimeIcon() {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
    )
    Box {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.anime_inner),
            contentDescription = stringResource(R.string.anime)
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.anime_outer),
            contentDescription = stringResource(R.string.anime),
            modifier = Modifier.graphicsLayer { rotationZ = angle }
        )
    }
}