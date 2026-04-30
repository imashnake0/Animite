package com.imashnake.animite.media

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imashnake.animite.api.anilist.sanitize.media.Info
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.component.CharacterCard
import com.imashnake.animite.core.ui.component.LoadingMediaSmall
import com.imashnake.animite.core.ui.component.Paginator
import com.imashnake.animite.core.ui.rememberDefaultPaddings
import com.imashnake.animite.media.ext.res
import kotlinx.collections.immutable.ImmutableList
import com.imashnake.animite.core.ui.R as coreUiR

@Composable
fun MediaMediumList(
    mediaMediumList: ImmutableList<Media.Medium>,
    onItemClick: (Int, String?) -> Unit,
    shouldShowRank: Boolean,
    modifier: Modifier = Modifier,
    pageInfo: Info? = null,
    onPageChanged: (Int) -> Unit = {},
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    var page by remember { mutableIntStateOf(pageInfo?.currentPage ?: -1) }
    LazyColumn(
        state = state,
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)
    ) {
        // Needed to prevent scrolling after item animations
        item(key = 0) {}
        items(mediaMediumList.size, key = { mediaMediumList[it].id }) { index ->
            val rank = if (!shouldShowRank) {
                null
            } else {
                (index + 1) + (pageInfo?.currentPage?.minus(1)?.times(10) ?: 1)
            }
            MediaMediumItem(
                item = mediaMediumList[index],
                onClick = onItemClick,
                rank = rank,
                modifier = Modifier
                    .animateItem()
                    .height(137.dp)
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = dimensionResource(coreUiR.dimen.media_card_corner_radius),
                            bottomStart = dimensionResource(coreUiR.dimen.media_card_corner_radius),
                            topEnd = if (index == 0) {
                                dimensionResource(coreUiR.dimen.media_card_corner_radius)
                            } else LocalPaddings.current.small,
                            bottomEnd = if (index == mediaMediumList.lastIndex) {
                                dimensionResource(coreUiR.dimen.media_card_corner_radius)
                            } else LocalPaddings.current.small,
                        )
                    )
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.025f))
            )
        }
        if (pageInfo?.currentPage != null && pageInfo.lastPage != null) {
            item {
                Paginator(
                    page = page,
                    hasNextPage = pageInfo.hasNextPage,
                    pageRange = 1..pageInfo.lastPage!!,
                    onPageChanged = {
                        page = it
                        onPageChanged(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MediaMediumItem(
    item: Media.Medium,
    onClick: (Int, String?) -> Unit,
    rank: Int?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.clickable { onClick(item.id, item.title) }) {
        if (rank != null) {
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .fillMaxHeight()
                    .width(LocalPaddings.current.large * 3)
            )
        }

        Row {
            rank?.let {
                Text(
                    text = rank.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(horizontal = LocalPaddings.current.tiny)
                        .requiredWidth(30.dp)
                        .align(Alignment.CenterVertically)
                )
            }

            CharacterCard(
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
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )

                item.season?.let {
                    Text(
                        text = stringResource(it.res) +
                                " ${item.seasonYear?.toString().orEmpty()}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                }

                Spacer(Modifier.size(LocalPaddings.current.medium))

                Text(
                    text = item.studios.joinToString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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

                    if (item.format != null && item.episodes != null) {
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
                                id = R.plurals.episode_count,
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
                            topStart = dimensionResource(coreUiR.dimen.media_card_corner_radius),
                            bottomStart = dimensionResource(coreUiR.dimen.media_card_corner_radius),
                            topEnd = if (index == 0) {
                                dimensionResource(coreUiR.dimen.media_card_corner_radius)
                            } else LocalPaddings.current.small,
                            bottomEnd = if (index == count - 1) {
                                dimensionResource(coreUiR.dimen.media_card_corner_radius)
                            } else LocalPaddings.current.small,
                        )
                    )
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.025f))
            )
        }
    }
}

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
