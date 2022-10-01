package com.imashnake.animite.features.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.type.MediaType
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.imashnake.animite.R as Res

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@RootNavGraph(start = true)
@Destination(style = HomeTransitions::class)
@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val homeMediaType = MediaType.ANIME

    viewModel.populateMediaLists(homeMediaType)

    val popularThisSeasonMediaList = viewModel.uiState.popularThisSeasonMediaList?.media
    val trendingNowMediaList = viewModel.uiState.trendingMediaList?.media
    val upcomingNextSeasonMediaList = viewModel.uiState.upcomingNextSeasonMediaList?.media

    when {
        trendingNowMediaList != null &&
        popularThisSeasonMediaList != null &&
        upcomingNextSeasonMediaList != null -> {
            Box(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
            ) {
                Box {
                    Image(
                        painter = painterResource(Res.drawable.background),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensionResource(Res.dimen.banner_height)),
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
                                        + dimensionResource(Res.dimen.backdrop_corner_radius)
                            )
                    )
                }

                // TODO: Use `padding` instead of the `Spacer`s.
                Column {
                    Spacer(
                        Modifier
                            .size(
                                dimensionResource(Res.dimen.banner_height)
                                        - dimensionResource(Res.dimen.backdrop_corner_radius)
                            )
                    )

                    Column(
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(
                                    topStart = dimensionResource(Res.dimen.backdrop_corner_radius),
                                    topEnd = dimensionResource(Res.dimen.backdrop_corner_radius)
                                )
                            )
                            .background(MaterialTheme.colorScheme.background)
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
                            mediaList = trendingNowMediaList,
                            onItemClick = { itemId ->
                                navigator.navigate(
                                    MediaPageDestination(
                                        id = itemId,
                                        mediaTypeArg = homeMediaType.rawValue
                                    )
                                ) {
                                    launchSingleTop = true
                                }
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
                            mediaList = popularThisSeasonMediaList,
                            onItemClick = { itemId ->
                                navigator.navigate(
                                    MediaPageDestination(
                                        id = itemId,
                                        mediaTypeArg = homeMediaType.rawValue
                                    )
                                ) {
                                    launchSingleTop = true
                                }
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
                            mediaList = upcomingNextSeasonMediaList,
                            onItemClick = { itemId ->
                                navigator.navigate(
                                    MediaPageDestination(
                                        id = itemId,
                                        mediaTypeArg = homeMediaType.rawValue
                                    )
                                ) {
                                    launchSingleTop = true
                                }
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
        }

        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // TODO: Unhardcode dimensions.
                LinearProgressIndicator(
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(100.dp)
                        .height(3.dp),
                    color = MaterialTheme.colorScheme.background,
                    trackColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
