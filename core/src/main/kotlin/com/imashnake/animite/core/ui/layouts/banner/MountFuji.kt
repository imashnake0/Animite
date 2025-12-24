package com.imashnake.animite.core.ui.layouts.banner

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.imashnake.animite.core.R

@Composable
fun MountFuji(modifier: Modifier = Modifier) {
    val extendedScreenWidth = LocalWindowInfo.current.containerSize.width + 100

    val infiniteTransition = rememberInfiniteTransition(label = "clouds")
    val durationMillis = 50000
    val delayMillis = durationMillis / 10
    val horizontalPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud_position"
    )
    val delayedHorizontalPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, delayMillis = delayMillis),
            repeatMode = RepeatMode.Reverse
        ),
        label = "delayed_cloud_position"
    )
    Box(
        modifier = modifier
            .background(
                verticalGradient(
                    0f to Color(0xFF7AAEDD),
                    0.75f to Color(0xFFA1C8F5),
                    1f to Color(0xFFD1E4F6)
                )
            )
            .fillMaxHeight()
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.cloud_2),
            contentDescription = null,
            modifier = Modifier
                .height(40.dp)
                .offset { IntOffset(x = extendedScreenWidth - 300, y = 25) }
                .graphicsLayer {
                    translationX = -horizontalPosition * extendedScreenWidth * 0.6f
                    alpha = 0.4f
                },
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.cloud_3),
            contentDescription = null,
            modifier = Modifier
                .height(45.dp)
                .offset { IntOffset(x = 300, y = 100) }
                .graphicsLayer {
                    translationX = -horizontalPosition * extendedScreenWidth * 0.4f
                    alpha = 0.6f
                },
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.cloud_1),
            contentDescription = null,
            modifier = Modifier
                .height(35.dp)
                .offset { IntOffset(x = 500, y = 230) }
                .graphicsLayer {
                    translationX = horizontalPosition * extendedScreenWidth * 0.6f
                    alpha = 0.5f
                },
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.mount_fuji),
            contentDescription = null,
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height) / 2.5f)
                .align(Alignment.BottomEnd)
                .graphicsLayer {
                    // TODO: Fix miter and remove this.
                    translationY = 4f
                },
            alignment = Alignment.TopCenter,
            contentScale = ContentScale.Crop
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.cloud_2),
            contentDescription = null,
            modifier = Modifier
                .height(40.dp)
                .offset { IntOffset(x = -110, y = 320) }
                .graphicsLayer {
                    translationX = delayedHorizontalPosition * extendedScreenWidth * 0.8f
                    alpha = 0.9f
                },
        )

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.cloud_1),
            contentDescription = null,
            modifier = Modifier
                .height(35.dp)
                .offset { IntOffset(x = extendedScreenWidth, y = 350) }
                .graphicsLayer {
                    translationX = -delayedHorizontalPosition * extendedScreenWidth * 0.8f
                },
        )
    }
}


@Preview
@Composable
fun PreviewMountFuji() {
    MountFuji(
        modifier = Modifier
            .height(dimensionResource(R.dimen.banner_height))
            .fillMaxWidth()
    )
}
