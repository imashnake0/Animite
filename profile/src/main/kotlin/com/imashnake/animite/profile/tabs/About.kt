package com.imashnake.animite.profile.tabs

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.boswelja.markdown.material3.m3TextStyles
import com.imashnake.animite.api.anilist.sanitize.profile.User
import core.extensions.landscapeCutoutPadding
import core.ui.LocalPaddings
import core.ui.StatsRow
import com.imashnake.animite.profile.R
import kotlinx.coroutines.launch

@Composable
fun AboutTab(
    user: User,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val statsLabelToValue = listOf(
        stringResource(R.string.total_anime) to user.count?.toString(),
        stringResource(R.string.days_watched) to user.daysWatched?.let { "%.1f".format(it) },
        stringResource(R.string.mean_score) to user.meanScore?.let { "%.1f".format(it) }
    ).filter { it.second != null }

    // TODO: Cleanup this layout.
    Column(
        modifier
            .verticalScroll(scrollState)
            .landscapeCutoutPadding()
            .padding(bottom = LocalPaddings.current.large)
    ) {
        Column(
            modifier = Modifier.padding(LocalPaddings.current.large),
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
        ) {
            StatsRow(
                stats = statsLabelToValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .landscapeCutoutPadding()
            ) {
                Text(
                    text = it.first,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )

                it.second?.let { value ->
                    Text(
                        text = value,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displaySmall
                    )
                }
            }

            if (user.genres.isNotEmpty()) Genres(user.genres)
        }
    }
}

@Composable
private fun Genres(
    genres: List<User.Genre>,
    modifier: Modifier = Modifier
) {
    val barWidthAnimation = remember { Animatable(0f) }
    val barAlphaAnimation = remember { Animatable(0f) }
    val barColor = MaterialTheme.colorScheme.primary
    LaunchedEffect("barAnimation") {
        launch {
            barWidthAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, delayMillis = 250)
            )
        }
        launch {
            barAlphaAnimation.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, delayMillis = 250)
            )
        }
    }

    Text(
        text = stringResource(R.string.genres),
        style = MaterialTheme.typography.titleMedium
    )
    Row(modifier.height(IntrinsicSize.Max)) {
        Column(horizontalAlignment = Alignment.End) {
            genres.forEach {
                Text(
                    text = it.genre,
                    style = m3TextStyles().textStyle.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f)
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    start = LocalPaddings.current.small,
                    end = LocalPaddings.current.large
                )
                .widthIn(max = 250.dp),
            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny)
        ) {
            val highestCount = genres.maxOf { it.mediaCount }
            genres.forEach {
                val weight = it.mediaCount/highestCount.toFloat()
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = weight)
                        .weight(1f)
                        .graphicsLayer { alpha = weight * barAlphaAnimation.value }
                        .drawBehind {
                            drawRoundRect(
                                color = barColor,
                                size = Size(
                                    width = size.width * barWidthAnimation.value,
                                    height = size.height
                                ),
                                cornerRadius = CornerRadius(size.height)
                            )
                        },
                )
            }
        }
    }
}
