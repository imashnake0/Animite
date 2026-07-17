package com.imashnake.animite.profile.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.Media.ListNames.Companion.sanitize
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.MediaTrackingCard
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.media.ext.res
import com.imashnake.animite.profile.R
import com.imashnake.animite.profile.dev.res
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MediaTrackingLists(
    type: Media.Small.Type,
    namedLists: ImmutableList<User.MediaCollection.NamedTrackingList>,
    listVisibility: SnapshotStateMap<Int, Boolean>,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)
    ) {
        namedLists.fastForEachIndexed { index, namedList ->
            stickyHeader(key = namedList.name) {
                namedList.name?.let {
                    Box(
                        modifier = Modifier
                            .height(dimensionResource(R.dimen.tracking_list_header_height))
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .clickable {
                                listVisibility[index]?.let { visibility ->
                                    listVisibility[index] = !visibility
                                }
                            }
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f))
                            .animateItem()
                    ) {
                        val iconPadding = (dimensionResource(R.dimen.tracking_list_header_height)
                                - dimensionResource(R.dimen.tracking_list_header_icon_size)) / 2
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(iconPadding)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(it.sanitize().res),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(dimensionResource(R.dimen.tracking_list_header_icon_size))
                            )
                            Text(
                                text = it,
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
                                text = namedList.list.size.toString(),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyMedium.copy(baselineShift = null),
                            )

                            val iconRotation by animateFloatAsState(if (listVisibility[index] ?: true) 0f else -90f)
                            // TODO: Move to core:ui and reuse from explore.
                            Icon(
                                painter = painterResource(R.drawable.drop_down),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .requiredSize(16.dp)
                                    .graphicsLayer { rotationZ = iconRotation }
                            )
                        }
                    }
                }
            }
            if (listVisibility[index] ?: true) {
                items(namedList.list.size, key = { namedList.list[it].id }) {
                    MediaTrackingItem(
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
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.tracking_list_header_height) / 2)
                            .animateItem()
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MediaTrackingItem(
    item: Media.Tracking,
    onClick: (Int, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.clickable { onClick(item.id, item.title) }) {
        Row {
            MediaTrackingCard(
                image = item.coverImage,
                tag = null,
                label = null,
                onClick = { onClick(item.id, item.title) },
                tagMinLines = 1
            )

            Column(
                Modifier.padding(
                    horizontal = LocalPaddings.current.large / 2,
                    vertical = LocalPaddings.current.small
                )
            ) {
                Text(
                    text = item.title.orEmpty(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )

                Spacer(Modifier.size(LocalPaddings.current.medium))

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
                        Divider()
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

                    if (item.episodes != null && item.season != null) {
                        Divider()
                    }

                    item.episodes?.let {
                        Text(
                            text = pluralStringResource(
                                id = R.plurals.ep_count,
                                count = it,
                                formatArgs = arrayOf(it)
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        item.score?.let { score ->
            Text(
                text = score.value.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(score.color).copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(end = LocalPaddings.current.medium)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Divider(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "divider")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    Box(
        modifier = modifier
            .graphicsLayer { rotationZ = angle }
            .padding(horizontal = LocalPaddings.current.small)
            .size(4.dp)
            .background(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f),
                shape = MaterialShapes.Triangle.toShape()
            )
    )
}
