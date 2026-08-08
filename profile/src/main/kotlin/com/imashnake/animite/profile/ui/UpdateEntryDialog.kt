package com.imashnake.animite.profile.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.Confirm
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.ToggleOn
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastRoundToInt
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.api.anilist.type.ScoreFormat
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.Divider
import com.imashnake.animite.core.ui.component.DropDownIcon
import com.imashnake.animite.core.ui.component.MediaMediumCard
import com.imashnake.animite.core.ui.ext.crossfadeModel
import com.imashnake.animite.media.ext.res
import com.imashnake.animite.profile.R
import com.imashnake.animite.profile.dev.GREEN
import com.imashnake.animite.profile.dev.LIME
import com.imashnake.animite.profile.dev.ORANGE
import com.imashnake.animite.profile.dev.RED
import com.imashnake.animite.profile.dev.res
import com.imashnake.animite.profile.dev.title
import me.saket.cascade.CascadeDropdownMenu
import me.saket.cascade.rememberCascadeState
import com.imashnake.animite.media.R as mediaR

// TODO: This padding cannot be removed.
private val DropdownMenuItemHorizontalPadding = 12.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UpdateEntryDialog(
    item: Media.Tracking,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStatus by remember { mutableStateOf(item.status) }
    var currentScore by remember { mutableStateOf(item.score) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(18.dp + LocalPaddings.current.large),
            modifier = modifier.fillMaxWidth()
        ) {
            Header(
                title = item.title,
                coverImage = item.coverImage,
                bannerImage = item.bannerImage,
                format = item.format,
                season = item.season,
                seasonYear = item.seasonYear,
                tintColor = item.color?.toColorInt()
                    ?.let { int -> Color(int) }
                    ?.copy(alpha = 0.25f)
                    ?: Color.Transparent
            )

            Column(
                modifier = Modifier.padding(
                    horizontal = LocalPaddings.current.large,
                    vertical = LocalPaddings.current.medium
                )
            ) {
                Box(Modifier.fillMaxWidth()) {
                    StatusDropDown(
                        type = item.type,
                        selectedStatus = selectedStatus,
                        onSelectStatus = { selectedStatus = it },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = LocalPaddings.current.medium)
                    )

                    currentScore?.let {
                        SetScore(
                            score = it,
                            onScoreSet = { value, format ->
                                currentScore = currentScore?.copy(
                                    value = value,
                                    normalizedValue = Media.Score.normalizeValue(value, format)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .align(Alignment.CenterEnd)
                                .padding(start = LocalPaddings.current.medium)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Header(
    coverImage: String?,
    title: String?,
    bannerImage: String?,
    format: Media.Format?,
    season: Media.Season?,
    seasonYear: Int?,
    tintColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Column {
            Box {
                Box(
                    modifier = Modifier
                        .background(tintColor)
                        .fillMaxWidth()
                        .aspectRatio(19f / 4)
                )

                AsyncImage(
                    model = crossfadeModel(bannerImage),
                    contentDescription = null,
                    error = painterResource(mediaR.drawable.background),
                    fallback = painterResource(mediaR.drawable.background),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(
                        color = tintColor,
                        blendMode = BlendMode.SrcAtop
                    ),
                    modifier = Modifier.fillMaxWidth().aspectRatio(19f / 4)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
                modifier = Modifier.padding(
                    start = LocalPaddings.current.large + 96.dp + LocalPaddings.current.medium,
                    top = LocalPaddings.current.medium,
                    end = LocalPaddings.current.medium
                )
            ) {
                Text(
                    text = title.orEmpty(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    format?.let {
                        Text(
                            text = stringResource(it.res),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (season != null && format != null) {
                        Divider(shape = MaterialShapes.Triangle.toShape())
                    }

                    season?.let {
                        Text(
                            text = stringResource(it.res) +
                                    " ${seasonYear?.toString().orEmpty()}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        MediaMediumCard(
            image = coverImage,
            tag = null,
            label = null,
            onClick = {},
            tagMinLines = 1,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    top = LocalPaddings.current.large,
                    start = LocalPaddings.current.large
                )
        )
    }
}

// TODO: This should be an actual dropdown.
@Composable
fun StatusDropDown(
    type: Media.Small.Type,
    selectedStatus: User.TrackingStatus,
    onSelectStatus: (User.TrackingStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val statuses = if (type == Media.Small.Type.ANIME) {
        User.TrackingStatus.animeStatuses()
    } else User.TrackingStatus.mangaStatuses()
    val cascadeState = rememberCascadeState()
    var isStatusDropDownExpanded by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val iconPadding = (dimensionResource(R.dimen.tracking_list_header_height) - dimensionResource(R.dimen.tracking_list_header_icon_size)) / 2
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(dimensionResource(R.dimen.tracking_list_header_height))
            .clip(CircleShape)
            .clickable {
                isStatusDropDownExpanded = true
                haptic.performHapticFeedback(ToggleOn)
            }
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh.copy(
                    alpha = 0.95f
                )
            )
            .padding(iconPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DropdownMenuItemHorizontalPadding + 4.dp),
        ) {
            AnimatedContent(selectedStatus) {
                Icon(
                    imageVector = ImageVector.vectorResource(it.res),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(dimensionResource(R.dimen.tracking_list_header_icon_size))
                )
            }
            AnimatedContent(selectedStatus) {
                Text(
                    text = stringResource(it.title),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium.copy(baselineShift = null),
                    overflow = TextOverflow.Ellipsis,
                    // DropDownIcon padding
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
        DropDownIcon(isDroppedDown = isStatusDropDownExpanded)
    }

    CascadeDropdownMenu(
        expanded = isStatusDropDownExpanded,
        onDismissRequest = { isStatusDropDownExpanded = false },
        state = cascadeState,
        shape = RoundedCornerShape(
            (dimensionResource(R.dimen.tracking_list_header_height) + LocalPaddings.current.tiny) / 2
        ),
        offset = DpOffset(x = 0.dp, y = LocalPaddings.current.tiny),
        modifier = Modifier.padding(vertical = LocalPaddings.current.tiny)
    ) {
        statuses.fastForEach {
            val selectedAlpha by animateFloatAsState(
                targetValue = if (it == selectedStatus) 0.95f else 0f
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(it.title),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium.copy(baselineShift = null),
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(it.res),
                        contentDescription = stringResource(it.title),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(dimensionResource(R.dimen.tracking_list_header_icon_size))
                    )
                },
                onClick = {
                    onSelectStatus(it)
                    haptic.performHapticFeedback(Confirm)
                    isStatusDropDownExpanded = false
                },
                contentPadding = PaddingValues(horizontal = iconPadding),
                modifier = Modifier
                    .padding(horizontal = LocalPaddings.current.tiny)
                    .clip(
                        shape = RoundedCornerShape(
                            size = dimensionResource(R.dimen.tracking_list_header_height)
                                    - LocalPaddings.current.tiny
                        )
                    )
                    .background(color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = selectedAlpha))
                    .height(dimensionResource(R.dimen.tracking_list_header_height))
            )
        }
    }
}

@Composable
private fun SetScore(
    score: Media.Score,
    onScoreSet: (value: Float, format: ScoreFormat) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        val scoreColor by animateColorAsState(
            targetValue = Color(
                when {
                    score.normalizedValue < 0.3f -> RED
                    score.normalizedValue < 0.6f -> ORANGE
                    score.normalizedValue < 0.8f -> LIME
                    else -> GREEN
                }
            )
        )

        // TODO: Add buttons to increment/decrement.
        Text(
            text = when (score.format) {
                ScoreFormat.POINT_100,
                ScoreFormat.POINT_10 -> score.value.toInt()
                ScoreFormat.POINT_10_DECIMAL -> (score.value.toFloat() * 10f).fastRoundToInt() / 10f
                else -> ""
            }.toString(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = scoreColor.copy(alpha = 0.8f),
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(0.5f)
        )

        Slider(
            value = score.value.toFloat(),
            onValueChange = { onScoreSet(it, score.format) },
            valueRange = when(score.format) {
                ScoreFormat.POINT_100 -> 0f..100f
                ScoreFormat.POINT_10_DECIMAL -> 0f..10f
                ScoreFormat.POINT_10 -> 0f..10f
                ScoreFormat.POINT_5 -> 0f..5f
                ScoreFormat.POINT_3 -> 0f..3f
                else -> 0f..0f
            },
            steps = when(score.format) {
                ScoreFormat.POINT_100 -> 99
                ScoreFormat.POINT_10_DECIMAL -> 99
                ScoreFormat.POINT_10 -> 9
                ScoreFormat.POINT_5 -> 4
                ScoreFormat.POINT_3 -> 2
                else -> 0
            },
            track = { state ->
                SliderDefaults.Track(
                    sliderState = state,
                    colors = colors(
                        activeTrackColor = Color.Transparent,
                        inactiveTrackColor = Color.Transparent,
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent
                    ),
                    modifier = Modifier.drawBehind {
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                0.3f to Color(RED),
                                0.6f to Color(ORANGE),
                                0.8f to Color(LIME),
                                1f to Color(GREEN),
                            ),
                            cornerRadius = CornerRadius(x = 50f, y = 50f)
                        )
                    }
                )
            },
            thumb = { state ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(start = (1f - (state.value / state.valueRange.endInclusive)) * 16.dp, end = (state.value / state.valueRange.endInclusive) * 16.dp).size(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .size(10.dp)
                    )
                }
            }
        )
    }
}
