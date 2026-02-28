package com.imashnake.animite.manga

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.window.core.layout.WindowSizeClass
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.extensions.horizontalOnly
import com.imashnake.animite.core.extensions.plus
import com.imashnake.animite.core.ui.LoadingMediaSmallRow
import com.imashnake.animite.core.ui.LocalPaddings
import com.imashnake.animite.core.ui.MediaCard
import com.imashnake.animite.core.ui.MediaSmallRow
import com.imashnake.animite.core.ui.layouts.TranslucentStatusBarLayout
import com.imashnake.animite.core.ui.layouts.banner.BannerLayout
import com.imashnake.animite.core.ui.layouts.banner.MountFuji
import com.imashnake.animite.media.MediaPage
import com.imashnake.animite.media.search.MediaSearchBar
import com.imashnake.animite.media.search.MediaSearchBar
import com.imashnake.animite.navigation.SharedContentKey
import com.imashnake.animite.navigation.SharedContentKey.Component.Card
import com.imashnake.animite.navigation.SharedContentKey.Component.Image
import com.imashnake.animite.navigation.SharedContentKey.Component.Page
import kotlinx.collections.immutable.ImmutableList
import com.imashnake.animite.navigation.R as navigationR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("LongMethod")
fun MangaScreen(
    windowSizeClass: WindowSizeClass,
    onNavigateToMediaItem: (MediaPage) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    contentWindowInsets: WindowInsets = WindowInsets.systemBars.union(WindowInsets.displayCutout),
    viewModel: MangaViewModel = hiltViewModel(),
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

    val dayHour by viewModel.dayHour.collectAsState(initial = null)

    val trendingList by viewModel.trendingMedia.collectAsState()
    val allTimePopularList by viewModel.allTimePopular.collectAsState()

    val rows = listOf(
        trendingList,
        allTimePopularList,
    )

    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

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
                        MountFuji(
                            setDayHour = dayHour,
                            header = stringResource(R.string.okaeri),
                            insetPaddingValues = insetPaddingValues,
                            navigationComponentPaddingValues = navigationComponentPaddingValues,
                            modifier = bannerModifier,
                        )
                    },
                    content = {
                        MediaSearchBar(
                            mediaType = MediaType.MANGA,
                            onItemClick = { item ->
                                onNavigateToMediaItem(
                                    MediaPage(
                                        id = item.id,
                                        source = MediaList.Type.SEARCH.name,
                                        mediaType = MediaType.MANGA.rawValue,
                                        title = item.title,
                                    )
                                )
                            },
                            windowSizeClass = windowSizeClass,
                            modifier = Modifier
                                .padding(insetAndNavigationPaddingValues.horizontalOnly)
                                .padding(horizontal = LocalPaddings.current.large)
                                .align(Alignment.CenterHorizontally)
                        )
                        rows.fastForEachIndexed { index, row ->
                            AnimatedContent(
                                targetState = row is Resource.Success,
                                transitionSpec = {
                                    fadeIn(tween(500, delayMillis = index * 100))
                                        .togetherWith(fadeOut(tween(500)))
                                },
                            ) {
                                if (it) {
                                    val mediaList = (row as? Resource.Success)?.data
                                    if (mediaList?.list.orEmpty().isNotEmpty()) {
                                        MangaRow(
                                            items = mediaList!!.list,
                                            type = mediaList.type,
                                            onItemClicked = { media ->
                                                onNavigateToMediaItem(
                                                    MediaPage(
                                                        id = media.id,
                                                        source = mediaList.type.name,
                                                        mediaType = MediaType.MANGA.rawValue,
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
                                    }
                                } else {
                                    LoadingMediaSmallRow(
                                        count = 10,
                                        contentPadding = PaddingValues(
                                            horizontal = LocalPaddings.current.large,
                                            vertical = LocalPaddings.current.large / 2,
                                        ) + insetAndNavigationPaddingValues.horizontalOnly
                                    )
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

@Composable
private fun MangaRow(
    items: ImmutableList<Media.Small>,
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
                tag = null,
                label = media.title,
                onClick = { onItemClicked(media) },
                modifier = Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        SharedContentKey(
                            id = media.id,
                            source = type.name,
                            sharedComponents = Card to Page,
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
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
