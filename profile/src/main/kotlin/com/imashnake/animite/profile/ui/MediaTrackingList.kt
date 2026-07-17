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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.profile.User
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.LoadingMediaSmall
import com.imashnake.animite.core.ui.component.MediaTrackingCard
import com.imashnake.animite.core.ui.rememberDefaultPaddings
import com.imashnake.animite.media.ext.res
import com.imashnake.animite.profile.R
import com.imashnake.animite.profile.dev.res
import kotlinx.collections.immutable.ImmutableList
import com.imashnake.animite.media.R as mediaR

@Composable
fun MediaTrackingLists(
    namedLists: ImmutableList<User.MediaCollection.NamedTrackingList>,
    onItemClick: (Int, String?) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    val listVisibility = remember { mutableStateMapOf(*List(namedLists.size) { it to true }.toTypedArray()) }
    LazyColumn(
        state = state,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)
    ) {
        namedLists.fastForEachIndexed { index, namedList ->
            // TODO: See if you can change the radius:
            //  https://medium.com/@yuriyskul/how-to-track-and-detect-sticky-headers-stuck-states-in-jetpack-compose-f4f2499f2ae8
            stickyHeader(key = namedList.name) {
                namedList.name?.let {
                    Box(
                        modifier = Modifier
                            // TODO: Deal with hard dimens.
                            .height(70.dp)
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                // TODO: Deal with hard dimens.
                                .padding(start = 25.dp)
                        ) {
                            Icon(
                                // TODO: Cleanup
                                imageVector = ImageVector.vectorResource(try {
                                    Names.valueOf(it.uppercase()).res
                                } catch (_: Exception) { com.imashnake.animite.navigation.R.drawable.anime }),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                // TODO: Deal with hard dimens.
                                modifier = Modifier.size(20.dp)
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
                            // TODO: Deal with hard dimens.
                            modifier = Modifier
                                .padding(end = 25.dp)
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
                        onClick = onItemClick,
                        modifier = Modifier
                            // TODO: Deal with hard dimens.
                            .padding(horizontal = 35.dp)
                            .animateItem()
                            // TODO: Deal with hard dimens.
                            .height(80.dp)
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
                            modifier = Modifier
                                .graphicsLayer { rotationZ = angle }
                                .padding(horizontal = LocalPaddings.current.small)
                                .size(4.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f),
                                    shape = MaterialShapes.Cookie4Sided.toShape()
                                )
                        )
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
                            modifier = Modifier
                                .graphicsLayer { rotationZ = angle }
                                .padding(horizontal = LocalPaddings.current.small)
                                .size(4.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f),
                                    shape = MaterialShapes.Cookie4Sided.toShape()
                                )
                        )
                    }

                    item.episodes?.let {
                        Text(
                            text = pluralStringResource(
                                id = mediaR.plurals.episode_count,
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
    }
}

@Composable
fun LoadingMediaMediumItem(
    shouldShowRank: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        if (shouldShowRank) {
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .requiredHeight(137.dp)
                    .width(LocalPaddings.current.large * 3)
            )
        }

        Row {
            if (shouldShowRank)
                Text(
                    text = " ",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(horizontal = LocalPaddings.current.tiny)
                        .requiredWidth(30.dp)
                        .align(Alignment.CenterVertically)
                )

            LoadingMediaSmall(
                imageHeight = 137.dp,
                cardWidth = 96.dp,
                shouldShowLabel = false
            )
        }
    }
}

@Composable
fun LoadingMediaMediumList(
    count: Int,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small),
        contentPadding = contentPadding,
        userScrollEnabled = false,
        modifier = modifier
    ) {
        item(key = 0) {}
        items(count) { index ->
            LoadingMediaMediumItem(
                shouldShowRank = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            bottomStart = 18.dp,
                            topEnd = if (index == 0) {
                                18.dp
                            } else LocalPaddings.current.small,
                            bottomEnd = if (index == count - 1) {
                                18.dp
                            } else LocalPaddings.current.small,
                        )
                    )
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.025f))
            )
        }
    }
}

// TODO: Cleanup
@Preview
@Composable
fun PreviewLoadingMediaMediumList() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        LoadingMediaMediumList(count = 10)
    }
}

@Preview
@Composable
fun PreviewLoadingMediaMediumItem() {
    CompositionLocalProvider(LocalPaddings provides rememberDefaultPaddings()) {
        LoadingMediaMediumItem(
            shouldShowRank = true,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(LocalPaddings.current.small))
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.025f))
        )
    }
}

// TODO: Move this.
enum class Names {
    COMPLETED,
    PAUSED,
    DROPPED,
    REWATCHING,
    PLANNING
}
