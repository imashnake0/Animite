package com.imashnake.animite.settings

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import kotlinx.serialization.Serializable
import com.imashnake.animite.core.R as coreR

@Composable
fun SettingsPage(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val extendedScreenWidth = LocalWindowInfo.current.containerSize.width + 100

    val infiniteTransition = rememberInfiniteTransition(label = "clouds")
    val durationMillis = 50000
    val delayMillis = durationMillis / 10
    val horizontalPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud_position"
    )
    val delayedHorizontalPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, delayMillis = delayMillis, easing = EaseOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "delayed_cloud_position"
    )

    TranslucentStatusBarLayout(scrollState) {
        Box(modifier.verticalScroll(scrollState)) {
            BannerLayout(
                banner = { bannerModifier ->
                    Box(
                        modifier = bannerModifier
                            .background(Color(0xFF7AAEDD))
                            .fillMaxHeight()
                    ) {
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
                                .height(dimensionResource(coreR.dimen.banner_height) / 2.5f)
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
                },
                content = {
                    Text("what")
                }
            )
        }
    }
}

@Serializable
data object SettingsPage
