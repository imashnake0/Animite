package com.imashnake.animite.media.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.core.ui.CharacterCard
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.media.R
import com.imashnake.animite.media.ext.res
import com.imashnake.animite.core.R as coreR

@Composable
fun SearchResults(
    items: Result<List<Media.Medium>>,
    onItemClick: (Media.Medium) -> Unit,
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AnimatedVisibility(
            visible = loading,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        AnimatedContent(
            targetState = items,
            modifier = Modifier.fillMaxWidth(),
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) {
            it.fold(
                onSuccess = {
                    if (it.isNotEmpty()) {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                vertical = LocalPaddings.current.large
                            ),
                            verticalArrangement = Arrangement.spacedBy(LocalPaddings.current.medium)
                        ) {
                            items(
                                items = it,
                                key = { it.id }
                            ) { media ->
                                MediaMediumItem(
                                    item = media,
                                    onClick = { onItemClick(media) },
                                    modifier = Modifier.padding(horizontal = LocalPaddings.current.large)
                                )
                            }
                        }
                    } else if (!loading) {
                        // Empty state is only visible when not loading
                        SearchEmptyContent(Modifier.fillMaxWidth())
                    }
                },
                onFailure = {
                    SearchErrorContent(it.message, modifier = Modifier.fillMaxWidth())
                }
            )
        }
    }
}

@Composable
private fun SearchEmptyContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(LocalPaddings.current.medium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.search_results_empty),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SearchErrorContent(
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(LocalPaddings.current.medium),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage ?: stringResource(R.string.search_results_error),
            style = MaterialTheme.typography.bodyMedium
        )
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
        CharacterCard(
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

            if (item.studios.isNotEmpty()) {
                Text(
                    text = item.studios.joinToString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

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
