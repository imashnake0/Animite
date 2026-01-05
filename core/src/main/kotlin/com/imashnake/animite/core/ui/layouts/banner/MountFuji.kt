package com.imashnake.animite.core.ui.layouts.banner

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.imashnake.animite.core.R
import com.imashnake.animite.core.extensions.DayPart
import com.imashnake.animite.core.extensions.DayPart.*
import com.imashnake.animite.core.extensions.copy
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.toDayPart
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.rememberDefaultPaddings
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun MountFuji(
    dayPart: DayPart?,
    header: String? = null,
    insetPaddingValues: PaddingValues = PaddingValues(),
    navigationComponentPaddingValues: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
) {
    val currentDayPart = dayPart ?: Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toDayPart()

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

    val stops = when (currentDayPart) {
        MORNING -> Triple(0xFF007695, 0xFFC8C4A3, 0xFFFFE8A5)
        AFTERNOON -> Triple(0xFF7AAEDD, 0xFFA1C8F5, 0xFFD1E4F6)
        EVENING -> Triple(0xFF5F81E2, 0xFFBF98B7, 0xFFCDACC2)
        NIGHT -> Triple(0xFF001020, 0xFF112B3A, 0xFF32434B)
    }

    val mountFujiDrawable = when (currentDayPart) {
        MORNING -> R.drawable.mount_fuji_morning
        AFTERNOON -> R.drawable.mount_fuji_afternoon
        EVENING -> R.drawable.mount_fuji_evening
        NIGHT -> R.drawable.mount_fuji_night
    }

    val headerColor = when (currentDayPart) {
        MORNING -> 0xFFFFFAE2
        AFTERNOON -> 0xFFFAFAFA
        EVENING -> 0xFFE0E0FF
        NIGHT -> 0xFFAEACA7
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

        if (currentDayPart == AFTERNOON) {
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

        header?.let {
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
                    text = it,
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

        if (currentDayPart == AFTERNOON || currentDayPart == MORNING) {
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
        }

        if (currentDayPart == AFTERNOON) {
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
}


@Preview
@Composable
fun PreviewMountFujiMorning() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        MountFuji(
            dayPart = MORNING,
            header = "Settings",
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height))
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun PreviewMountFujiAfternoon() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        MountFuji(
            dayPart = AFTERNOON,
            header = "Settings",
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height))
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun PreviewMountFujiEvening() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        MountFuji(
            dayPart = EVENING,
            header = "Settings",
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height))
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun PreviewMountFujiNight() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        MountFuji(
            dayPart = NIGHT,
            header = "Settings",
            modifier = Modifier
                .height(dimensionResource(R.dimen.banner_height))
                .fillMaxWidth()
        )
    }
}
