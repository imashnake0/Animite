package com.imashnake.animite.profile.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.ToggleOff
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.ToggleOn
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMapNotNull
import androidx.compose.ui.util.fastRoundToInt
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.api.anilist.sanitize.profile.User.TrackingStatus.Companion.sanitize
import com.imashnake.animite.api.anilist.type.ScoreFormat
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.Divider
import com.imashnake.animite.core.ui.component.DropDownIcon
import com.imashnake.animite.core.ui.component.MediaTrackingCard
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.media.ext.res
import com.imashnake.animite.profile.R
import com.imashnake.animite.profile.dev.res
import kotlinx.collections.immutable.ImmutableList
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.math.roundToInt

@Composable
fun MediaTrackingLists(
    type: Media.Small.Type,
    namedLists: ImmutableList<User.MediaCollection.NamedTrackingList>,
    listVisibility: SnapshotStateMap<Int, Boolean>,
    updateMediaListsOrder: (List<String>) -> Unit,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    useExpressiveProgressIndicator: Boolean,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    val namedLists = remember(namedLists) { namedLists.toMutableStateList() }

    val haptic = LocalHapticFeedback.current
    var isReordering by remember { mutableStateOf(false) }
    val reorderableLazyListState = rememberReorderableLazyListState(state) { from, to ->
        namedLists.apply { add(to.index - 1, removeAt(from.index - 1)) }
        haptic.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    LazyColumn(
        state = state,
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        item {
            ListOptions(
                isReordering = isReordering,
                expandAll = { listVisibility.forEach { (index, _) -> listVisibility[index] = true } },
                collapseAll = { listVisibility.forEach { (index, _) -> listVisibility[index] = false } },
                setIsReordering = { isReordering = it },
                onDone = { updateMediaListsOrder(namedLists.fastMapNotNull { it.name }.toList()) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        namedLists.fastForEachIndexed { index, namedList ->
            stickyHeader(key = namedList.name) {
                namedList.name?.let {
                    ReorderableItem(reorderableLazyListState, key = it) { _ ->
                        Column(Modifier.animateItem()) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(LocalPaddings.current.small)
                                    .background(
                                        if (isReordering) {
                                            Color.Transparent
                                        } else MaterialTheme.colorScheme.background
                                    )
                            )
                            HeaderPill(
                                name = it,
                                size = namedList.list.size,
                                index = index,
                                listVisibility = listVisibility,
                                isReordering = isReordering,
                                reorderScope = this@ReorderableItem
                            )
                        }
                    }
                }
            }
            if (listVisibility[index] ?: true) {
                items(namedList.list.size, key = { index.toString() + namedList.list[it].id }) {
                    Column(Modifier.animateItem()) {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(LocalPaddings.current.small)
                                .background(MaterialTheme.colorScheme.background)
                        )
                        MediaTrackingItem(
                            listName = namedList.name.sanitize(),
                            item = namedList.list[it],
                            onClick = { id, title ->
                                onNavigateToMediaItem(
                                    MediaPage(
                                        id = id,
                                        source = "${namedList.name}" + type.type,
                                        mediaType = type.name,
                                        title = title
                                    )
                                )
                            },
                            useExpressiveProgressIndicator = useExpressiveProgressIndicator,
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(R.dimen.tracking_list_header_height) / 2)
                                .height(dimensionResource(R.dimen.tracking_list_item_height))
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 18.dp,
                                        bottomStart = 18.dp,
                                        topEnd = if (it == 0) 18.dp else LocalPaddings.current.small,
                                        bottomEnd = if (it == namedList.list.lastIndex) {
                                            18.dp
                                        } else LocalPaddings.current.small,
                                    )
                                )
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.025f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ListOptions(
    isReordering: Boolean,
    expandAll: () -> Unit,
    collapseAll: () -> Unit,
    setIsReordering: (Boolean) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(top = LocalPaddings.current.small)
            .padding(vertical = LocalPaddings.current.small)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)) {
            val alpha by animateFloatAsState(if (isReordering) 0.3f else 1f)
            ListOption(
                icon = ImageVector.vectorResource(R.drawable.expand_all),
                text = stringResource(R.string.expand_all),
                onClick = { if (!isReordering) expandAll() },
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
            )
            ListOption(
                text = stringResource(R.string.collapse_all),
                onClick = { if (!isReordering) collapseAll() },
                icon = ImageVector.vectorResource(R.drawable.collapse_all),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha)
            )
        }

        AnimatedContent(isReordering) {
            if (it) {
                ListOption(
                    icon = Icons.Rounded.Check,
                    text = stringResource(R.string.done),
                    onClick = {
                        onDone()
                        setIsReordering(false)
                    },
                    background = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(end = 10.dp)
                )
            } else {
                ListOption(
                    icon = ImageVector.vectorResource(R.drawable.reorder),
                    text = stringResource(R.string.reorder),
                    onClick = { collapseAll(); setIsReordering(true) }
                )
            }
        }
    }
}

@Composable
private fun ListOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val haptic = LocalHapticFeedback.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
        modifier = modifier
            .clip(CircleShape)
            .background(background)
            .clickable { haptic.performHapticFeedback(HapticFeedbackType.ContextClick); onClick() }
            .padding(LocalPaddings.current.tiny)
            .padding(end = LocalPaddings.current.tiny)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(dimensionResource(R.dimen.list_options_icon_size))
        )
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.labelSmall.copy(baselineShift = null),
        )
    }
}

@Composable
private fun HeaderPill(
    name: String,
    size: Int,
    index: Int,
    listVisibility: SnapshotStateMap<Int, Boolean>,
    isReordering: Boolean,
    reorderScope: ReorderableCollectionItemScope,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .height(dimensionResource(R.dimen.tracking_list_header_height))
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable {
                haptic.performHapticFeedback(
                    if (listVisibility[index] == true) ToggleOff else ToggleOn
                )
                if (!isReordering) {
                    listVisibility[index]?.let { visibility ->
                        listVisibility[index] = !visibility
                    }
                }
            }
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh.copy(
                    alpha = 0.95f
                )
            )
    ) {
        val iconPadding =
            (dimensionResource(R.dimen.tracking_list_header_height)
                    - dimensionResource(R.dimen.tracking_list_header_icon_size)) / 2
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(iconPadding)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(name.sanitize().res),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(dimensionResource(R.dimen.tracking_list_header_icon_size))
            )
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(baselineShift = null),
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
            modifier = Modifier
                .padding(end = iconPadding)
                .align(Alignment.CenterEnd)
        ) {
            Text(
                text = size.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodyMedium.copy(baselineShift = null),
            )

            AnimatedContent(isReordering) {
                if (!it) {
                    DropDownIcon(isDroppedDown = listVisibility[index] ?: true)
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.drag_indicator),
                        contentDescription = null,
                        modifier = with(reorderScope) {
                            modifier.requiredSize(16.dp).draggableHandle()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MediaTrackingItem(
    listName: User.TrackingStatus,
    item: Media.Tracking,
    useExpressiveProgressIndicator: Boolean,
    onClick: (Int, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isUpdateEntryDialogVisible by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.combinedClickable(
            onClick = { onClick(item.id, item.title) },
            // TODO: Add this after the point system is corrected.
//            onLongClick = { isUpdateEntryDialogVisible = true }
        )
    ) {
        MediaTrackingCard(
            image = item.coverImage,
            tag = null,
            label = null,
            onClick = { onClick(item.id, item.title) },
            tagMinLines = 1
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(
                    start = LocalPaddings.current.large / 2,
                    end = LocalPaddings.current.large / 2,
                    top = LocalPaddings.current.small
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.tiny),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = item.title.orEmpty(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        item.format?.let {
                            Text(
                                text = stringResource(it.res),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (item.season != null && item.format != null) {
                            Divider(shape = MaterialShapes.Triangle.toShape())
                        }

                        item.season?.let {
                            Text(
                                text = stringResource(it.res) +
                                        " ${item.seasonYear?.toString().orEmpty()}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelSmall,
                                maxLines = 1
                            )
                        }
                    }
                }

                item.score?.let { score ->
                    Score(score)
                }
            }

            val progress = item.progress
            val segments = item.segments ?: if (progress == 0) 1 else progress

            if (segments != null && progress != null) {
                val formattedProgress = progress
                    .takeUnless { listName == User.TrackingStatus.COMPLETED }
                    ?.let { "$it/$segments" }
                    ?: "$segments"

                Row(
                    horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formattedProgress,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    when (listName) {
                        User.TrackingStatus.WATCHING,
                        User.TrackingStatus.REWATCHING,
                        User.TrackingStatus.READING,
                        User.TrackingStatus.REREADING -> if (useExpressiveProgressIndicator) {
                            LinearWavyProgressIndicator(
                                progress = { progress.toFloat() / segments },
                                amplitude = { if (it <= 0.1f || it >= 0.95f) 0f else 0.5f },
                                waveSpeed = 15.dp,
                                modifier = Modifier.fillMaxWidth().graphicsLayer { alpha = 0.6f }
                            )
                        } else {
                            LinearProgressIndicator(
                                progress = { progress.toFloat() / segments },
                                modifier = Modifier.fillMaxWidth().graphicsLayer { alpha = 0.6f }
                            )
                        }
                        else -> LinearProgressIndicator(
                            progress = { progress.toFloat() / segments },
                            modifier = Modifier.fillMaxWidth().graphicsLayer { alpha = 0.6f }
                        )
                    }
                }
            }
        }
    }

    if (isUpdateEntryDialogVisible) {
        UpdateEntryDialog(
            item = item,
            onDismissRequest = { isUpdateEntryDialogVisible = false }
        )
    }
}

@Composable
fun RowScope.Score(
    score: Media.Score,
    modifier: Modifier = Modifier
) {
    when(score.format) {
        ScoreFormat.POINT_100,
        ScoreFormat.POINT_10_DECIMAL,
        ScoreFormat.POINT_10 -> {
            Text(
                text = when (score.format) {
                    ScoreFormat.POINT_100,
                    ScoreFormat.POINT_10 -> score.value.roundToInt()
                    ScoreFormat.POINT_10_DECIMAL -> score.value
                    else -> ""
                }.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(score.color).copy(alpha = 0.6f),
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 5.dp)
            )
        }
        ScoreFormat.POINT_5 -> {}
        ScoreFormat.POINT_3 -> {
            Icon(
                imageVector = ImageVector.vectorResource(
                    when(score.value.fastRoundToInt()) {
                        0 -> R.drawable.dead
                        1 -> R.drawable.weary
                        2 -> R.drawable.neutral
                        else -> R.drawable.smile
                    }
                ),
                contentDescription = null,
                tint = Color(score.color).copy(alpha = 0.6f),
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .size(dimensionResource(R.dimen.smiley_icon_size))
            )
        }
        else -> {}
    }
}
