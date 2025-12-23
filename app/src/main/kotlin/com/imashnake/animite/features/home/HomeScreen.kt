package com.imashnake.animite.features.home

import android.content.res.Configuration
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.R
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.extensions.copy
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaCard
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.core.ui.ProgressIndicatorScreen
import com.imashnake.animite.core.ui.layouts.BannerLayout
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.core.ui.shaders.etherealShader
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.navigation.SharedContentKey
import com.imashnake.animite.navigation.SharedContentKey.Component.Card
import com.imashnake.animite.navigation.SharedContentKey.Component.Image
import com.imashnake.animite.navigation.SharedContentKey.Component.Page
import com.imashnake.animite.settings.MountFuji
import com.imashnake.animite.media.R as mediaR
import com.imashnake.animite.navigation.R as navigationR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongMethod")
fun HomeScreen(
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val insetPaddingValues = contentWindowInsets.asPaddingValues()
    val navigationComponentPaddingValues = when(LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PaddingValues(
            bottom = dimensionResource(navigationR.dimen.navigation_bar_height)
        )
        else -> PaddingValues(
            start = dimensionResource(navigationR.dimen.navigation_rail_width)
        )
    }
    val insetAndNavigationPaddingValues = insetPaddingValues + navigationComponentPaddingValues

    val homeMediaType by rememberSaveable { mutableStateOf(MediaType.ANIME) }
    viewModel.setMediaType(homeMediaType)

    val trendingList by viewModel.trendingMedia.collectAsState()
    val popularList by viewModel.popularMediaThisSeason.collectAsState()
    val upcomingList by viewModel.upcomingMediaNextSeason.collectAsState()
    val allTimePopularList by viewModel.allTimePopular.collectAsState()

    val rows = listOf(
        trendingList,
        popularList,
        upcomingList,
        allTimePopularList,
    )

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    when {
        rows.all { it is Resource.Success } -> {
            val scrollState = rememberScrollState()
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refresh { isRefreshing = it } },
                state = pullToRefreshState,
            ) {
                TranslucentStatusBarLayout(scrollState) {
                    Box(Modifier.verticalScroll(scrollState)) {
                        BannerLayout(
                            banner = { bannerModifier ->
                                Box {
                                    MountFuji(bannerModifier)

                                    Row(
                                        modifier = bannerModifier
                                            .padding(
                                                horizontal = LocalPaddings.current.large,
                                                vertical = LocalPaddings.current.medium
                                            )
                                            .padding(insetPaddingValues.copy(bottom = 0.dp)),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom,
                                    ) {
                                        Text(
                                            text = stringResource(R.string.okaeri),
                                            color = Color(0xB5001626),
                                            style = MaterialTheme.typography.displayMedium,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier
                                                .weight(1f, fill = false)
                                                .padding(navigationComponentPaddingValues.horizontalOnly),
                                            maxLines = 1
                                        )
                                    }
                                }
                            },
                            content = {
                                rows.fastForEach { row ->
                                    row.data?.let {
                                        AnimatedContent(
                                            targetState = it,
                                            transitionSpec = {
                                                fadeIn(tween(750)).togetherWith(fadeOut(tween(750)))
                                            },
                                            label = "animate_home_row"
                                        ) { mediaList ->
                                            if (mediaList.list.isNotEmpty()) {
                                                HomeRow(
                                                    items = mediaList.list,
                                                    type = mediaList.type,
                                                    onItemClicked = { media ->
                                                        onNavigateToMediaItem(
                                                            MediaPage(
                                                                id = media.id,
                                                                source = mediaList.type.name,
                                                                mediaType = homeMediaType.rawValue,
                                                                title = media.title,
                                                            )
                                                        )
                                                    },
                                                    sharedTransitionScope = sharedTransitionScope,
                                                    animatedVisibilityScope = animatedVisibilityScope,
                                                    contentPadding = PaddingValues(
                                                        horizontal = LocalPaddings.current.large,
                                                        vertical = LocalPaddings.current.large / 2,
                                                    ) + insetAndNavigationPaddingValues.horizontalOnly
                                                )
                                            } else {
                                                Box(Modifier.fillMaxWidth())
                                            }
                                        }
                                    }
                                }
                            },
                            contentPadding = PaddingValues(
                                top = LocalPaddings.current.large / 2,
                                bottom = LocalPaddings.current.large / 2 +
                                        insetAndNavigationPaddingValues.calculateBottomPadding()
                            ),
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        )
                    }
                }
            }
        }
        else -> ProgressIndicatorScreen(Modifier.padding(insetPaddingValues))
    }
}

@Composable
fun HomeRow(
    items: List<Media.Small>,
    type: MediaList.Type,
    onItemClicked: (Media.Small) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    MediaSmallRow(
        title = type.title,
        mediaList = items,
        modifier = modifier,
        contentPadding = contentPadding,
    ) { _, media ->
        with(sharedTransitionScope) {
            MediaCard(
                image = media.coverImage,
                label = media.title,
                onClick = { onItemClicked(media) },
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(
                        SharedContentKey(
                            id = media.id,
                            source = type.name,
                            sharedComponents = Card to Page,
                        )
                    ),
                    animatedVisibilityScope,
                    enter = fadeIn(tween(500)),
                    exit = fadeOut(tween(500)),
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                ),
                imageModifier = Modifier.sharedBounds(
                    rememberSharedContentState(
                        SharedContentKey(
                            id = media.id,
                            source = type.name,
                            sharedComponents = Image to Image,
                        )
                    ),
                    animatedVisibilityScope,
                ),
                textModifier = Modifier.skipToLookaheadSize(),
            )
        }
    }
}

// TODO: Add this back in an intuitive way.
@Composable
@Suppress("CognitiveComplexMethod")
private fun MediaTypeSelector(
    modifier: Modifier = Modifier,
    selectedOption: MutableState<MediaType>,
    viewModel: HomeViewModel
) {
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.onBackground,
            shape = CircleShape
        )
    ) {
        val offset by animateDpAsState(
            targetValue = if (selectedOption.value == MediaType.ANIME) 0.dp else 40.dp,
            label = "media_switch"
        )

        Box(
            modifier = Modifier
                .padding(dimensionResource(mediaR.dimen.media_type_selector_padding))
                .size(dimensionResource(mediaR.dimen.media_type_choice_size))
                .offset { IntOffset(x = offset.roundToPx(), y = 0) }
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
        )

        Row(
            modifier = Modifier
                .height(dimensionResource(mediaR.dimen.media_type_selector_height))
                .width(dimensionResource(mediaR.dimen.media_type_selector_width))
                .padding(dimensionResource(mediaR.dimen.media_type_selector_padding)),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediaType.knownEntries.forEach { mediaType ->
                IconButton(
                    onClick = {
                        if (selectedOption.value != mediaType) {
                            viewModel.setMediaType(mediaType)
                            selectedOption.value = mediaType
                        }
                    },
                    modifier = Modifier.requiredWidth(dimensionResource(mediaR.dimen.media_type_choice_size))
                ) {
                    Icon(
                        imageVector = when (mediaType) {
                            MediaType.ANIME -> Icons.Rounded.PlayArrow
                            else -> ImageVector.vectorResource(id = R.drawable.manga)
                        },
                        contentDescription = mediaType.name,
                        tint = animateColorAsState(
                            targetValue = when(selectedOption.value) {
                                mediaType -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.background
                            },
                            animationSpec = tween(400),
                            label = "icon_color"
                        ).value
                    )
                }
            }
        }
    }
}
