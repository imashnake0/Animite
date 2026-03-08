package com.imashnake.animite.banner

import android.annotation.SuppressLint
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.imashnake.animite.banner.shaders.nightSky
import com.imashnake.animite.banner.shaders.sun
import com.imashnake.animite.core.ui.DayPart
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.LocalTimeContext
import com.imashnake.animite.core.ui.TimeContext
import com.imashnake.animite.core.ui.ext.copy
import com.imashnake.animite.core.ui.ext.horizontalOnly
import com.imashnake.animite.core.ui.produceTimeContext
import com.imashnake.animite.core.ui.rememberDefaultPaddings
import kotlin.math.PI
import kotlin.time.ExperimentalTime

private const val PI_FLOAT = PI.toFloat()

@OptIn(ExperimentalTime::class)
@Composable
fun MountFuji(
    header: String?,
    modifier: Modifier = Modifier,
    insetPaddingValues: PaddingValues = PaddingValues(),
    navigationComponentPaddingValues: PaddingValues = PaddingValues(),
    timeContext: TimeContext = LocalTimeContext.current,
) {
    val isNotNight by remember(timeContext) {
        derivedStateOf {
            timeContext.dayPart != DayPart.NIGHT
        }
    }
    val sunShader = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        remember(isNotNight) {
            RuntimeShader(
                when {
                    isNotNight -> sun
                    else -> nightSky
                }
            )
        }
    } else null

    val time by animateFloatAsState(timeContext.dayProgress * 2f * PI_FLOAT)

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

    val cloud1 = ImageVector.vectorResource(R.drawable.cloud_1)
    val cloud2 = ImageVector.vectorResource(R.drawable.cloud_2)
    val cloud3 = ImageVector.vectorResource(R.drawable.cloud_3)

    AnimatedContent(
        targetState = timeContext.dayPart,
        transitionSpec = {
            fadeIn(animationSpec = tween(700)).togetherWith(
                fadeOut(animationSpec = tween(700))
            )
        },
        modifier = Modifier
    ) {
        val stops = when (it) {
            DayPart.MORNING -> Triple(0xFF007695, 0xFFC8C4A3, 0xFFFFE8A5)
            DayPart.AFTERNOON -> Triple(0xFF7AAEDD, 0xFFA1C8F5, 0xFFD1E4F6)
            DayPart.EVENING -> Triple(0xFF5F81E2, 0xFFBF98B7, 0xFFCDACC2)
            DayPart.NIGHT -> Triple(0xFF001020, 0xFF112B3A, 0xFF32434B)
        }

        val mountFujiDrawable = when (it) {
            DayPart.MORNING -> R.drawable.mount_fuji_morning
            DayPart.AFTERNOON -> R.drawable.mount_fuji_afternoon
            DayPart.EVENING -> R.drawable.mount_fuji_evening
            DayPart.NIGHT -> R.drawable.mount_fuji_night
        }

        val headerColor = when (it) {
            DayPart.MORNING -> 0xFFFFFAE2
            DayPart.AFTERNOON -> 0xFFFAFAFA
            DayPart.EVENING -> 0xFFE0E0FF
            DayPart.NIGHT -> 0xFFAEACA7
        }

        Box(
            modifier = modifier
                .background(
                    verticalGradient(
                        0f to Color(stops.first),
                        0.75f to Color(stops.second),
                        1f to Color(stops.third)
                    )
                )
                .fillMaxHeight()
        ) {
            if (sunShader != null) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(navigationComponentPaddingValues.horizontalOnly)
                        .drawWithCache @SuppressLint("NewApi") {
                            sunShader.run {
                                setFloatUniform(
                                    "resolution",
                                    size.width,
                                    size.height,
                                )
                                setFloatUniform(
                                    "radius",
                                    size.height / 2.7f,
                                )
                                setFloatUniform("time", time)
                                setFloatUniform("PI", PI_FLOAT)
                                setFloatUniform("sparsity", 0.99f)
                                onDrawBehind {
                                    drawRect(ShaderBrush(this@run))
                                }
                            }
                        }

                )
            }

            Image(
                imageVector = cloud2,
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
                imageVector = cloud3,
                contentDescription = null,
                modifier = Modifier
                    .height(45.dp)
                    .offset { IntOffset(x = 300, y = 100) }
                    .graphicsLayer {
                        translationX = -horizontalPosition * extendedScreenWidth * 0.4f
                        alpha = 0.6f
                    },
            )

            if (it == DayPart.AFTERNOON) {
                Image(
                    imageVector = cloud1,
                    contentDescription = null,
                    modifier = Modifier
                        .height(35.dp)
                        .offset { IntOffset(x = 500, y = 230) }
                        .graphicsLayer {
                            translationX = horizontalPosition * extendedScreenWidth * 0.6f
                            alpha = 0.5f
                        },
                )
            }

            Image(
                imageVector = ImageVector.vectorResource(mountFujiDrawable),
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

            header?.let { header ->
                Row(
                    modifier = modifier
                        .padding(
                            horizontal = LocalPaddings.current.large,
                            vertical = LocalPaddings.current.medium
                        )
                        .padding(insetPaddingValues.copy(bottom = 0.dp)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = header,
                        color = Color(headerColor),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .padding(navigationComponentPaddingValues.horizontalOnly),
                        maxLines = 1
                    )
                }
            }

            if (it == DayPart.AFTERNOON || it == DayPart.MORNING) {
                Image(
                    imageVector = cloud2,
                    contentDescription = null,
                    modifier = Modifier
                        .height(40.dp)
                        .offset { IntOffset(x = -110, y = 320) }
                        .graphicsLayer {
                            translationX = delayedHorizontalPosition * extendedScreenWidth * 0.8f
                            alpha = 0.9f
                        },
                )
            }

            if (it == DayPart.AFTERNOON) {
                // TODO: This doesn't show for landscape.
                Image(
                    imageVector = cloud1,
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
    }
}


@Preview
@Composable
fun PreviewMountFujiMorning() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        MountFuji(
            header = "Settings",
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height))
                .fillMaxWidth(),
            timeContext = TimeContext(0.3f, DayPart.MORNING)
        )
    }
}

@Preview
@Composable
fun PreviewMountFujiAfternoon() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        MountFuji(
            header = "Settings",
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height))
                .fillMaxWidth(),
            timeContext = TimeContext(0.5f, DayPart.AFTERNOON)
        )
    }
}

@Preview
@Composable
fun PreviewMountFujiEvening() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        MountFuji(
            header = "Settings",
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height))
                .fillMaxWidth(),
            timeContext = TimeContext(0.7f, DayPart.EVENING)
        )
    }
}

@Preview
@Composable
fun PreviewMountFujiNight() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        MountFuji(
            header = "Settings",
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height))
                .fillMaxWidth(),
            timeContext = TimeContext(0.9f, DayPart.NIGHT)
        )
    }
}

@Preview(device = "spec:width=800dp,height=200dp,dpi=240")
@Composable
fun PreviewMountFuji() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        MountFuji(
            header = "Settings",
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height))
                .fillMaxWidth(),
            timeContext = produceTimeContext().value
        )
    }
}
