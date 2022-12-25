package com.imashnake.animite.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.MediaListQuery
import com.imashnake.animite.core.extensions.bannerParallax
import com.imashnake.animite.core.extensions.landscapeCutoutPadding
import com.imashnake.animite.core.ui.ProgressIndicator
import com.imashnake.animite.core.ui.TranslucentStatusBarLayout
import com.imashnake.animite.data.Resource
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.features.media.MediaPageArgs
import com.imashnake.animite.features.ui.MediaSmall
import com.imashnake.animite.features.ui.MediaSmallRow
import com.imashnake.animite.type.MediaType
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.imashnake.animite.R as Res

@Destination
@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val homeMediaType = MediaType.ANIME

    viewModel.setMediaType(homeMediaType)

    val trendingList by viewModel.trendingMedia.collectAsState()
    val popularList by viewModel.popularMediaThisSeason.collectAsState()
    val upcomingList by viewModel.upcomingMediaNextSeason.collectAsState()
    val allTimePopularList by viewModel.allTimePopular.collectAsState()

    // TODO: [Code Smells: If Statements](https://dzone.com/articles/code-smells-if-statements).
    when {
        trendingList != null &&
        popularList != null &&
        upcomingList != null &&
        allTimePopularList != null -> {
            val scrollState = rememberScrollState()
            TranslucentStatusBarLayout(
                scrollState = scrollState,
                distanceUntilAnimated = dimensionResource(Res.dimen.banner_height)
            ) {
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
                                .bannerParallax(scrollState),
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
                                .landscapeCutoutPadding()
                        )
                    }

                    Column {
                        Spacer(Modifier.size(dimensionResource(Res.dimen.banner_height)))

                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .padding(vertical = dimensionResource(Res.dimen.large_padding))
                                // TODO move this one out of Home when we can pass modifiers in
                                .padding(bottom = dimensionResource(Res.dimen.navigation_bar_height)),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(Res.dimen.large_padding))
                        ) {
                            HomeRow(
                                list = (trendingList as? Resource.Success)?.data?.media.orEmpty(),
                                title = stringResource(Res.string.trending_now),
                                onItemClicked = {
                                    navigator.navigate(
                                        MediaPageDestination(
                                            MediaPageArgs(
                                                it.id,
                                                homeMediaType.rawValue
                                            )
                                        )
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                            )

                            HomeRow(
                                list = (popularList as? Resource.Success)?.data?.media.orEmpty(),
                                title = stringResource(Res.string.popular_this_season),
                                onItemClicked = {
                                    navigator.navigate(
                                        MediaPageDestination(
                                            MediaPageArgs(
                                                it.id,
                                                homeMediaType.rawValue
                                            )
                                        )
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                            )

                            HomeRow(
                                list = (upcomingList as? Resource.Success)?.data?.media.orEmpty(),
                                title = stringResource(Res.string.upcoming_next_season),
                                onItemClicked = {
                                    navigator.navigate(
                                        MediaPageDestination(
                                            MediaPageArgs(
                                                it.id,
                                                homeMediaType.rawValue
                                            )
                                        )
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                            )

                            HomeRow(
                                list = (allTimePopularList as? Resource.Success)?.data?.media.orEmpty(),
                                title = stringResource(Res.string.all_time_popular),
                                onItemClicked = {
                                    navigator.navigate(
                                        MediaPageDestination(
                                            MediaPageArgs(
                                                it.id,
                                                homeMediaType.rawValue
                                            )
                                        )
                                    ) {
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                ProgressIndicator()
            }
        }
    }
}

@Composable
fun HomeRow(
    list: List<MediaListQuery.Medium?>,
    title: String,
    onItemClicked: (MediaListQuery.Medium) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(
                start = dimensionResource(Res.dimen.large_padding)
            )
            .landscapeCutoutPadding()
        )

        Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

        MediaSmallRow(
            mediaList = list,
            content = { media ->
                MediaSmall(
                    image = media?.coverImage?.extraLarge,
                    // TODO: Do something about this chain.
                    label = media?.title?.romaji ?:
                    media?.title?.english ?:
                    media?.title?.native ?: "",
                    onClick = {
                        onItemClicked(media!!)
                    },
                    modifier = Modifier.width(dimensionResource(Res.dimen.media_card_width))
                )
            }
        )
    }
}
