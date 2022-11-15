package com.imashnake.animite.features.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.imashnake.animite.data.sauce.db.model.Media
import com.imashnake.animite.dev.ext.given
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.features.ui.MediaSmall
import com.imashnake.animite.features.ui.MediaSmallRow
import com.imashnake.animite.features.ui.ProgressIndicator
import com.imashnake.animite.type.MediaType
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.imashnake.animite.R as Res

@RootNavGraph(start = true)
@Destination(style = HomeTransitions::class)
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val homeMediaType = MediaType.ANIME
    viewModel.setMediaType(homeMediaType)

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(true)

    // true while mediaType is not set (Flow.filterNotNull) and up until the lists have emitted non-null and non-empty data
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProgressIndicator()
        }
    } else {
        // are these kind of lists "stable"?
        val trendingList by viewModel.trendingMedia.collectAsStateWithLifecycle()
        val popularList by viewModel.popularMediaThisSeason.collectAsStateWithLifecycle()
        val upcomingList by viewModel.upcomingMediaNextSeason.collectAsStateWithLifecycle()
        val allTimePopularList by viewModel.allTimePopular.collectAsStateWithLifecycle()

        val scrollState = rememberScrollState()
        Box {
            Box(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .navigationBarsPadding()
            ) {
                Box {
                    Image(
                        painter = painterResource(Res.drawable.background),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(Res.dimen.banner_height))
                            .graphicsLayer {
                                translationY = 0.7f * scrollState.value
                            },
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.secondaryContainer.copy(
                                            alpha = 0.5f
                                        )
                                    )
                                )
                            )
                            .fillMaxWidth()
                            .height(dimensionResource(Res.dimen.banner_height))
                    ) { }

                    Text(
                        text = stringResource(Res.string.okaeri),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(
                                start = dimensionResource(Res.dimen.large_padding),
                                bottom = dimensionResource(Res.dimen.medium_padding)
                            )
                            .given(
                                LocalConfiguration.current.orientation
                                    == Configuration.ORIENTATION_LANDSCAPE
                            ) {
                                displayCutoutPadding()
                            }
                    )
                }

                // TODO: Use `padding` instead of the `Spacer`s.
                Column {
                    Spacer(Modifier.size(dimensionResource(Res.dimen.banner_height)))

                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .given(
                                LocalConfiguration.current.orientation
                                    == Configuration.ORIENTATION_LANDSCAPE
                            ) {
                                displayCutoutPadding()
                            }
                            .padding(vertical = dimensionResource(Res.dimen.large_padding))
                            // TODO move this one out of Home when we can pass modifiers in
                            .padding(bottom = dimensionResource(Res.dimen.navigation_bar_height)),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(Res.dimen.large_padding))
                    ) {

                        HomeRow(
                            list = trendingList,
                            title = stringResource(Res.string.trending_now),
                            onItemClicked = {
                                navigator.navigate(
                                    MediaPageDestination(
                                        id = it.id,
                                        mediaTypeArg = homeMediaType.rawValue
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        )

                        HomeRow(
                            list = popularList,
                            title = stringResource(Res.string.popular_this_season),
                            onItemClicked = {
                                navigator.navigate(
                                    MediaPageDestination(
                                        id = it.id,
                                        mediaTypeArg = homeMediaType.rawValue
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        )

                        HomeRow(
                            list = upcomingList,
                            title = stringResource(Res.string.upcoming_next_season),
                            onItemClicked = {
                                navigator.navigate(
                                    MediaPageDestination(
                                        id = it.id,
                                        mediaTypeArg = homeMediaType.rawValue
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        )

                        HomeRow(
                            list = allTimePopularList,
                            title = stringResource(Res.string.all_time_popular),
                            onItemClicked = {
                                navigator.navigate(
                                    MediaPageDestination(
                                        id = it.id,
                                        mediaTypeArg = homeMediaType.rawValue
                                    )
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }

            // Translucent status bar.
            val bannerHeight = with(LocalDensity.current) {
                dimensionResource(Res.dimen.banner_height).toPx()
            }
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = 0.75f * if (scrollState.value < bannerHeight) {
                            scrollState.value.toFloat() / bannerHeight
                        } else 1f
                    }
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .height(
                        WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
                    .align(Alignment.TopCenter)
            ) { }
        }
    }
}

@Composable
fun HomeRow(
    list: List<Media>,
    title: String,
    onItemClicked: (Media) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(
                start = dimensionResource(Res.dimen.large_padding)
            )
        )

        Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

        MediaSmallRow(
            mediaList = list,
            content = { media ->
                MediaSmall(
                    image = media.coverImage?.extraLarge,
                    // TODO: Do something about this chain.
                    label = media.title?.romaji ?: media.title?.english ?: media.title?.native ?: "",
                    onClick = {
                        onItemClicked(media)
                    },
                    modifier = Modifier.width(dimensionResource(Res.dimen.media_card_width))
                )
            }
        )
    }
}
