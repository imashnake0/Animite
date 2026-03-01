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
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaCard
import com.imashnake.animite.media.ext.res
import kotlinx.collections.immutable.ImmutableList
import com.imashnake.animite.core.R as coreR

@Composable
fun MediaMediumList(
    mediaMediumList: ImmutableList<Media.Medium>,
    onItemClick: (Media.Medium) -> Unit,
    modifier: Modifier = Modifier,
    searchBarHeight: Dp = 0.dp,
    searchBarBottomPadding: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(),
) {
    LazyColumn(
        modifier = modifier
            .consumeWindowInsets(
                PaddingValues(
                    bottom = searchBarBottomPadding + contentPadding.calculateBottomPadding()
                )
            )
            .imePadding(),
        contentPadding = PaddingValues(
            LocalPaddings.current.large
        ) + PaddingValues(
            bottom = LocalPaddings.current.large +
                    searchBarHeight +
                    searchBarBottomPadding
        ) + contentPadding,
        verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.small)
    ) {
        items(mediaMediumList.size, key = { mediaMediumList[it].id }) { index ->
            val item = mediaMediumList[index]
            MediaMediumItem(
                item = item,
                onClick = { onItemClick(item) },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MediaMediumItem(
    item: Media.Medium,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(coreR.dimen.media_card_corner_radius)))
            .clickable(onClick = onClick)
    ) {
        MediaCard(
            image = item.coverImage,
            tag = null,
            label = null,
            onClick = onClick,
        )

        Column(Modifier.padding(horizontal = LocalPaddings.current.small)) {
            Text(
                text = item.title.orEmpty(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 2
            )
            if (item.seasonYear != null) {
                Text(
                    text = item.seasonYear.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelSmall
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
                        style = MaterialTheme.typography.bodyMedium,
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
                            .size(6.dp)
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
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
