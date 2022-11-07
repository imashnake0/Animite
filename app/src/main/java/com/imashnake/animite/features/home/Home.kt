package com.imashnake.animite.features.home

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.imashnake.animite.dev.ext.given
import com.imashnake.animite.features.ui.MediaSmall
import com.imashnake.animite.features.ui.MediaSmallRow
import com.imashnake.animite.features.ui.ProgressIndicator
import com.imashnake.animite.type.MediaType
import com.imashnake.animite.R as Res

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeMediaType = MediaType.ANIME

    viewModel.populateMediaLists(homeMediaType)

    val trendingList = viewModel.uiState.trendingList?.media
    val popularList = viewModel.uiState.popularList?.media
    val upcomingList = viewModel.uiState.upcomingList?.media
    val allTimePopularList = viewModel.uiState.allTimePopularList?.media

    // TODO: [Code Smells: If Statements](https://dzone.com/articles/code-smells-if-statements).
    when {
        trendingList != null &&
        popularList != null &&
        upcomingList != null &&
        allTimePopularList != null -> {
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
                        ) {
                            Spacer(Modifier.size(dimensionResource(Res.dimen.large_padding)))

                            Text(
                                text = stringResource(Res.string.trending_now),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    start = dimensionResource(Res.dimen.large_padding)
                                )
                            )

                            Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

                            MediaSmallRow(
                                mediaList = trendingList,
                                onItemClick = { itemId ->
//                                    navigator.navigate(
//                                        MediaPageDestination(
//                                            id = itemId,
//                                            mediaTypeArg = homeMediaType.rawValue
//                                        )
//                                    ) {
//                                        launchSingleTop = true
//                                    }
                                },
                                content = { media, onItemClick ->
                                    MediaSmall(
                                        height = dimensionResource(Res.dimen.media_card_height),
                                        width = dimensionResource(Res.dimen.media_card_width),
                                        image = media?.coverImage?.extraLarge,
                                        // TODO: Do something about this chain.
                                        label = media?.title?.romaji ?:
                                        media?.title?.english ?:
                                        media?.title?.native ?: "",
                                        onClick = {
                                            onItemClick(media?.id)
                                        }
                                    )
                                }
                            )

                            Spacer(Modifier.size(dimensionResource(Res.dimen.large_padding)))

                            Text(
                                text = stringResource(Res.string.popular_this_season),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    start = dimensionResource(Res.dimen.large_padding)
                                )
                            )

                            Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

                            MediaSmallRow(
                                mediaList = popularList,
                                onItemClick = { itemId ->
//                                    navigator.navigate(
//                                        MediaPageDestination(
//                                            id = itemId,
//                                            mediaTypeArg = homeMediaType.rawValue
//                                        )
//                                    ) {
//                                        launchSingleTop = true
//                                    }
                                },
                                content = { media, onItemClick ->
                                    MediaSmall(
                                        height = dimensionResource(Res.dimen.media_card_height),
                                        width = dimensionResource(Res.dimen.media_card_width),
                                        image = media?.coverImage?.extraLarge,
                                        // TODO: Do something about this chain.
                                        label = media?.title?.romaji ?:
                                        media?.title?.english ?:
                                        media?.title?.native ?: "",
                                        onClick = {
                                            onItemClick(media?.id)
                                        }
                                    )
                                }
                            )

                            Spacer(Modifier.size(dimensionResource(Res.dimen.large_padding)))

                            Text(
                                text = stringResource(Res.string.upcoming_next_season),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    start = dimensionResource(Res.dimen.large_padding)
                                )
                            )

                            Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

                            MediaSmallRow(
                                mediaList = upcomingList,
                                onItemClick = { itemId ->
//                                    navigator.navigate(
//                                        MediaPageDestination(
//                                            id = itemId,
//                                            mediaTypeArg = homeMediaType.rawValue
//                                        )
//                                    ) {
//                                        launchSingleTop = true
//                                    }
                                },
                                content = { media, onItemClick ->
                                    MediaSmall(
                                        height = dimensionResource(Res.dimen.media_card_height),
                                        width = dimensionResource(Res.dimen.media_card_width),
                                        image = media?.coverImage?.extraLarge,
                                        // TODO: Do something about this chain.
                                        label = media?.title?.romaji ?:
                                        media?.title?.english ?:
                                        media?.title?.native ?: "",
                                        onClick = {
                                            onItemClick(media?.id)
                                        }
                                    )
                                }
                            )

                            Spacer(Modifier.size(dimensionResource(Res.dimen.large_padding)))

                            Text(
                                text = stringResource(Res.string.all_time_popular),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(
                                    start = dimensionResource(Res.dimen.large_padding)
                                )
                            )

                            Spacer(Modifier.size(dimensionResource(Res.dimen.medium_padding)))

                            MediaSmallRow(
                                mediaList = allTimePopularList,
                                onItemClick = { itemId ->
//                                    navigator.navigate(
//                                        MediaPageDestination(
//                                            id = itemId,
//                                            mediaTypeArg = homeMediaType.rawValue
//                                        )
//                                    ) {
//                                        launchSingleTop = true
//                                    }
                                },
                                content = { media, onItemClick ->
                                    MediaSmall(
                                        height = dimensionResource(Res.dimen.media_card_height),
                                        width = dimensionResource(Res.dimen.media_card_width),
                                        image = media?.coverImage?.extraLarge,
                                        // TODO: Do something about this chain.
                                        label = media?.title?.romaji ?:
                                        media?.title?.english ?:
                                        media?.title?.native ?: "",
                                        onClick = {
                                            onItemClick(media?.id)
                                        }
                                    )
                                }
                            )

                            Spacer(
                                Modifier.height(
                                    dimensionResource(Res.dimen.navigation_bar_height)
                                            + dimensionResource(Res.dimen.large_padding)
                                )
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
