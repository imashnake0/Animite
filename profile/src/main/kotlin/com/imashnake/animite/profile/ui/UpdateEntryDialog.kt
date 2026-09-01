package com.imashnake.animite.profile.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.Confirm
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.Reject
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.SegmentTick
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.ToggleOff
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.ToggleOn
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.util.fastCoerceIn
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastRoundToInt
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.imashnake.animite.api.anilist.EntryUpdateParams
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.Media.Companion.getFormattedDate
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.api.anilist.type.ScoreFormat
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.ConfirmButton
import com.imashnake.animite.core.ui.component.Divider
import com.imashnake.animite.core.ui.component.DropDownIcon
import com.imashnake.animite.core.ui.component.MediaMediumCard
import com.imashnake.animite.core.ui.component.RejectButton
import com.imashnake.animite.core.ui.ext.crossfadeModel
import com.imashnake.animite.media.ext.res
import com.imashnake.animite.profile.R
import com.imashnake.animite.profile.dev.GREEN
import com.imashnake.animite.profile.dev.LIME
import com.imashnake.animite.profile.dev.ORANGE
import com.imashnake.animite.profile.dev.RED
import com.imashnake.animite.profile.dev.res
import com.imashnake.animite.profile.dev.title
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import me.saket.cascade.CascadeDropdownMenu
import me.saket.cascade.rememberCascadeState
import kotlin.time.Instant
import com.imashnake.animite.media.R as mediaR
import com.imashnake.animite.settings.R as settingsR

private const val HALF_DAY_MILLIS = 86400000L

// TODO: This padding cannot be removed.
private val DropdownMenuItemHorizontalPadding = 12.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UpdateEntryDialog(
    item: Media.Tracking,
    updateEntry: (params: EntryUpdateParams) -> Unit,
    onDismissRequest: () -> Unit,
    useExpressiveProgressIndicator: Boolean,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    // TODO: Please move these along with logic to the VM.
    var selectedStatus by rememberSaveable { mutableStateOf(item.status) }
    var currentScore by rememberMediaScore(item.score)
    var currentProgress by rememberSaveable { mutableStateOf(item.progress) }
    var currentStartedAtDate by rememberFuzzyDate(item.startedAt)
    var currentCompletedAtDate by rememberFuzzyDate(item.completedAt)

    val startedAtDatePickerState = rememberDatePickerState(initialSelectedDateMillis = item.startedAt?.epochMillis)
    var isStartedAtDatePickerVisible by remember { mutableStateOf(false) }
    val completedAtDatePickerState = rememberDatePickerState(initialSelectedDateMillis = item.completedAt?.epochMillis)
    var isCompletedAtDatePickerVisible by remember { mutableStateOf(false) }

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

            Box {
                Column(
                    verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.large),
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = LocalPaddings.current.large,
                            vertical = LocalPaddings.current.large
                        )
                ) {
                    Section(title = stringResource(R.string.status)) {
                        StatusDropDown(
                            type = item.type,
                            selectedStatus = selectedStatus,
                            onSelectStatus = { selectedStatus = it }
                        )
                    }

                    Section(title = stringResource(R.string.score)) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            currentScore?.let {
                                when (it.format) {
                                    ScoreFormat.POINT_100,
                                    ScoreFormat.POINT_10_DECIMAL,
                                    ScoreFormat.POINT_10 -> {
                                        SetScore(
                                            score = it,
                                            onScoreSet = { value, format ->
                                                currentScore = currentScore?.copy(
                                                    value = when (format) {
                                                        ScoreFormat.POINT_100 -> value.fastCoerceIn(0f, 100f)
                                                        ScoreFormat.POINT_10,
                                                        ScoreFormat.POINT_10_DECIMAL -> value.fastCoerceIn(0f, 10f)
                                                        else -> value
                                                    },
                                                    normalizedValue = Media.Score.normalizeValue(value, format)
                                                )
                                                haptic.performHapticFeedback(SegmentTick)
                                            },
                                        )
                                    }

                                    ScoreFormat.POINT_5 -> {
                                        SetStars(
                                            score = it,
                                            onScoreSet = { value, format ->
                                                currentScore = currentScore?.copy(
                                                    value = value.fastCoerceIn(0f, 5f),
                                                    normalizedValue = Media.Score.normalizeValue(value, format)
                                                )
                                                haptic.performHapticFeedback(ToggleOn)
                                            }
                                        )
                                    }

                                    ScoreFormat.POINT_3 -> {
                                        SetSmileys(
                                            score = it,
                                            onScoreSet = { value, format ->
                                                currentScore = currentScore?.copy(
                                                    value = value.fastCoerceIn(0f, 3f),
                                                    normalizedValue = Media.Score.normalizeValue(value, format)
                                                )
                                                haptic.performHapticFeedback(ToggleOn)
                                            }
                                        )
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }

                    if (item.segments != null && currentProgress != null) {
                        val progress = currentProgress!!
                        val segments = item.segments ?: if (progress == 0) 1 else progress

                        Section(title = stringResource(R.string.progress)) {
                            Box(Modifier.padding(vertical = LocalPaddings.current.small)) {
                                ProgressBar(
                                    progress = progress,
                                    segments = segments,
                                    listName = selectedStatus,
                                    useExpressiveProgressIndicator = useExpressiveProgressIndicator,
                                    enabled = true,
                                    onProgressChanged = {
                                        currentProgress = it.fastRoundToInt()
                                        haptic.performHapticFeedback(SegmentTick)
                                    },
                                    // TODO: Figure out why this layout doesn't work. This makes space for ScoreButtons:
                                    modifier = Modifier.padding(
                                        end = 40.dp + LocalPaddings.current.small + LocalPaddings.current.medium
                                    )
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    ScoreButton(
                                        imageVector = ImageVector.vectorResource(R.drawable.minus),
                                        onClick = {
                                            currentProgress = (currentProgress!! - 1).fastCoerceIn(0, segments)
                                            haptic.performHapticFeedback(SegmentTick)
                                        },
                                        size = 20.dp
                                    )
                                    ScoreButton(
                                        imageVector = Icons.Rounded.Add,
                                        onClick = {
                                            currentProgress = (currentProgress!! + 1).fastCoerceIn(0, segments)
                                            haptic.performHapticFeedback(SegmentTick)
                                        },
                                        size = 20.dp
                                    )
                                }
                            }
                        }
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
                        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.large)
                    ) {
                        Section(
                            title = stringResource(R.string.started_at),
                            modifier = Modifier.weight(1f)
                        ) {
                            DateChip(
                                date = currentStartedAtDate?.formatted,
                                icon = ImageVector.vectorResource(R.drawable.calendar_created),
                                onClick = { isStartedAtDatePickerVisible = true },
                            )
                        }

                        Section(
                            title = stringResource(R.string.completed_at),
                            modifier = Modifier.weight(1f)
                        ) {
                            DateChip(
                                date = currentCompletedAtDate?.formatted,
                                icon = ImageVector.vectorResource(R.drawable.calendar_completed),
                                onClick = { isCompletedAtDatePickerVisible = true }
                            )
                        }
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Crossfade(
                            selectedStatus != item.status ||
                                    currentScore != item.score ||
                                    currentProgress != item.progress ||
                                    currentStartedAtDate != item.startedAt ||
                                    currentCompletedAtDate != item.completedAt
                        ) {
                            IconButton(
                                enabled = it,
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                ),
                                onClick = {
                                    haptic.performHapticFeedback(Reject)
                                    selectedStatus = item.status
                                    currentScore = item.score
                                    currentProgress = item.progress
                                    currentStartedAtDate = item.startedAt
                                    currentCompletedAtDate = item.completedAt
                                }
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.reset_settings),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(Modifier.size(LocalPaddings.current.small))
                        RejectButton(
                            imageVector = Icons.Rounded.Close,
                            text = stringResource(R.string.close),
                            onClick = {
                                haptic.performHapticFeedback(ToggleOff)
                                onDismissRequest()
                            },
                        )
                        Spacer(Modifier.size(LocalPaddings.current.medium))
                        ConfirmButton(
                            imageVector = ImageVector.vectorResource(R.drawable.save),
                            text = stringResource(R.string.save),
                            onClick = {
                                haptic.performHapticFeedback(Confirm)
                                updateEntry(
                                    EntryUpdateParams(
                                        id = item.id,
                                        status = selectedStatus,
                                        score = currentScore,
                                        progress = currentProgress,
                                        startedAt = currentStartedAtDate?.yearMonthDay,
                                        completedAt = currentCompletedAtDate?.yearMonthDay
                                    )
                                )
                                onDismissRequest()
                            },
                        )
                    }
                }

                // TODO: Make a reusable gradient scroll layout in core:ui.
                Box(
                    modifier = Modifier
                        .height(LocalPaddings.current.large)
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.surfaceContainerHighest,
                                    Color.Transparent
                                )
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .height(LocalPaddings.current.large)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surfaceContainerHighest
                                )
                            )
                        )
                )
            }
        }
    }

    if (isStartedAtDatePickerVisible) {
        DatePickerModal(
            state = startedAtDatePickerState,
            onDismiss = { isStartedAtDatePickerVisible = false },
            onDateSelected = { epochMillis ->
                val localDate = epochMillis?.let {
                    Instant
                        .fromEpochMilliseconds(it + HALF_DAY_MILLIS)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                }
                currentStartedAtDate = currentStartedAtDate?.copy(
                    formatted = localDate?.let {
                        getFormattedDate(it.year, it.month.number, it.day)
                    },
                    epochMillis = epochMillis,
                    yearMonthDay = localDate.let { Triple(it?.year, it?.month?.number, it?.day) }
                )
                startedAtDatePickerState.selectedDateMillis = epochMillis
            }
        )
    }

    if (isCompletedAtDatePickerVisible) {
        DatePickerModal(
            state = completedAtDatePickerState,
            onDismiss = { isCompletedAtDatePickerVisible = false },
            onDateSelected = { epochMillis ->
                val localDate = epochMillis?.let {
                    Instant
                        .fromEpochMilliseconds(it + HALF_DAY_MILLIS)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date
                }
                currentCompletedAtDate = currentCompletedAtDate?.copy(
                    formatted = localDate?.let {
                        getFormattedDate(it.year, it.month.number, it.day)
                    },
                    epochMillis = epochMillis,
                    yearMonthDay = localDate.let { Triple(it?.year, it?.month?.number, it?.day) }
                )
                completedAtDatePickerState.selectedDateMillis = epochMillis
            }
        )
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

// TODO: Reuse dropdown from the tabs.
@Composable
private fun StatusDropDown(
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

    var dropDownWidthDp by remember { mutableFloatStateOf(196f) }
    val density = LocalDensity.current

    Box(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { with(density) { dropDownWidthDp = it.width.toDp().value } }
                .defaultMinSize(minHeight = dimensionResource(R.dimen.tracking_list_header_height))
                .clip(CircleShape)
                .clickable {
                    isStatusDropDownExpanded = true
                    haptic.performHapticFeedback(ToggleOn)
                }
                .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f))
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
                        maxLines = 1,
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
            fixedWidth = dropDownWidthDp.dp,
            modifier = Modifier.padding(vertical = LocalPaddings.current.tiny)
        ) {
            statuses.fastForEach {
                val selectedAlpha by animateFloatAsState(
                    targetValue = if (it == selectedStatus) 1f else 0f
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
                        .clip(shape = RoundedCornerShape(size = dimensionResource(R.dimen.tracking_list_header_height) - LocalPaddings.current.tiny))
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(
                                alpha = selectedAlpha
                            )
                        )
                        .height(dimensionResource(R.dimen.tracking_list_header_height))
                )
            }
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
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium),
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            val step = when(score.format) {
                ScoreFormat.POINT_100,
                ScoreFormat.POINT_10 -> 1f
                ScoreFormat.POINT_10_DECIMAL -> 0.1f
                else -> 0f
            }

            ScoreButton(
                imageVector = ImageVector.vectorResource(R.drawable.minus),
                onClick = { onScoreSet(score.value - step, score.format) }
            )

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "10.0",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Transparent
                )
                // TODO: This should be a text field.
                Text(
                    text = when (score.format) {
                        ScoreFormat.POINT_100,
                        ScoreFormat.POINT_10 -> score.value.fastRoundToInt()
                        ScoreFormat.POINT_10_DECIMAL -> (score.value * 10f).fastRoundToInt() / 10f
                        else -> ""
                    }.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = scoreColor.copy(alpha = 0.8f),
                )
            }

            ScoreButton(
                imageVector = Icons.Rounded.Add,
                onClick = { onScoreSet(score.value + step, score.format) }
            )
        }

        CompositionLocalProvider(
            // Remove default M3 padding
            LocalMinimumInteractiveComponentSize provides 0.dp,
        ) {
            Slider(
                value = score.value,
                onValueChange = { onScoreSet(it, score.format) },
                valueRange = when (score.format) {
                    ScoreFormat.POINT_100 -> 0f..100f
                    ScoreFormat.POINT_10_DECIMAL -> 0f..10f
                    ScoreFormat.POINT_10 -> 0f..10f
                    ScoreFormat.POINT_5 -> 0f..5f
                    ScoreFormat.POINT_3 -> 0f..3f
                    else -> 0f..0f
                },
                track = { state ->
                    val trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
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
                                brush = SolidColor(trackColor),
                                cornerRadius = CornerRadius(x = 50f, y = 50f)
                            )
                        }
                    )
                },
                thumb = { state ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(
                            start = (1f - (state.value / state.valueRange.endInclusive)) * 16.dp,
                            end = (state.value / state.valueRange.endInclusive) * 16.dp
                        ).size(16.dp)
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
}

@Composable
private fun ScoreButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier
            .size(size)
            .clip(shape)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f))
    )
}

@Composable
private fun SetStars(
    score: Media.Score,
    onScoreSet: (value: Float, format: ScoreFormat) -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        repeat(5) {
            val alpha by animateFloatAsState(if (!(it + 1f <= score.value)) 0.2f else 1f)
            Icon(
                imageVector = ImageVector.vectorResource(settingsR.drawable.star),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
                modifier = modifier
                    .size(dimensionResource(R.dimen.dialog_star_icon_size))
                    .clickable(interactionSource = null, indication = null) {
                        onScoreSet(
                            if (score.value == it + 1f) { score.value - 1f } else it + 1f,
                            ScoreFormat.POINT_5
                        )
                    }
            )
        }
    }
}

@Composable
private fun SetSmileys(
    score: Media.Score,
    onScoreSet: (value: Float, format: ScoreFormat) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)) {
        repeat(4) {
            val color by animateColorAsState(
                targetValue = Color(
                    when(it) {
                        0 -> RED
                        1 -> ORANGE
                        2 -> LIME
                        else -> GREEN
                    }
                )
            )
            val alpha by animateFloatAsState(
                targetValue = if (score.value != it.toFloat()) 0.4f else 1f,
                animationSpec = tween(300)
            )

            Icon(
                imageVector = ImageVector.vectorResource(
                    when (it) {
                        0 -> R.drawable.dead
                        1 -> R.drawable.weary
                        2 -> R.drawable.neutral
                        else -> settingsR.drawable.smile
                    }
                ),
                contentDescription = null,
                tint = color.copy(alpha = 0.6f),
                modifier = modifier
                    .size(dimensionResource(R.dimen.smiley_icon_size))
                    .clip(CircleShape)
                    .clickable { onScoreSet(it.toFloat(), ScoreFormat.POINT_3) }
                    .graphicsLayer { this.alpha = alpha }
            )
        }
    }
}

@Composable
private fun DateChip(
    date: String?,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconPadding = (dimensionResource(R.dimen.date_picker_chip_height) - dimensionResource(R.dimen.date_picker_chip_icon_size)) / 2

    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = dimensionResource(R.dimen.date_picker_chip_height))
            .clip(CircleShape)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f))
            .padding(iconPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
            modifier = modifier.width(IntrinsicSize.Max)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(dimensionResource(R.dimen.date_picker_chip_icon_size))
            )

            if (date == null) {
                Text(
                    text = stringResource(R.string.add_date),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge.copy(baselineShift = null),
                    maxLines = 1,
                    modifier = Modifier.graphicsLayer { alpha = 0.5f }
                )
            } else {
                Text(
                    // The space is to add some buffer so that it flows to the next line
                    // before the text is clipped I think
                    text = "$date  ",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge.copy(baselineShift = null),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun DatePickerModal(
    state: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(state.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = state,
            showModeToggle = false
        )
    }
}

@Composable
private fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        modifier = modifier
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmallEmphasized,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        content()
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun rememberMediaScore(score: Media.Score?) = rememberSaveable(
    saver = Saver(
        save = {
            it.value?.let { score ->
                arrayListOf(score.value, score.normalizedValue, score.format)
            }
        },
        restore = {
            mutableStateOf(
                Media.Score(
                    value = it[0] as Float,
                    normalizedValue = it[1] as Float,
                    format = it[2] as ScoreFormat
                )
            )
        },
    )
) { mutableStateOf(score) }

@SuppressLint("ComposableNaming")
@Composable
fun rememberFuzzyDate(fuzzyDate: Media.FuzzyDate?) = rememberSaveable(
    saver = Saver(
        save = {
            it.value?.let { fuzzyDate ->
                arrayListOf(
                    fuzzyDate.formatted,
                    fuzzyDate.epochMillis,
                    fuzzyDate.yearMonthDay.first,
                    fuzzyDate.yearMonthDay.second,
                    fuzzyDate.yearMonthDay.third,
                )
            }
        },
        restore = {
            mutableStateOf(
                Media.FuzzyDate(
                    formatted = it[0] as String?,
                    epochMillis = it[1] as Long?,
                    yearMonthDay = Triple(it[2] as Int?, it[3] as Int?, it[4] as Int?)
                )
            )
        },
    )
) { mutableStateOf(fuzzyDate) }
