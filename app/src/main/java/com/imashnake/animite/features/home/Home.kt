package com.imashnake.animite.features.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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

    val popularThisSeasonMediaList = viewModel.uiState.popularMediaThisSeasonList?.media
    val trendingNowMediaList = viewModel.uiState.trendingMediaList?.media

    when {
        trendingNowMediaList != null && popularThisSeasonMediaList != null -> {
                Box(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                ) {
                    Box {
                        Image(
                            painter = painterResource(Res.drawable.background),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
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
                                .aspectRatio(1f)
                        ) {  }

                        Text(
                            text = stringResource(Res.string.okaeri),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(
                                    start = dimensionResource(Res.dimen.large_padding),
                                    bottom = dimensionResource(Res.dimen.large_padding)
                                            + dimensionResource(Res.dimen.backdrop_corner_radius)
                                )
                        )
                    }

                    // TODO: Use `verticalArrangement` instead of the `Spacer`s.
                    Column {
                        Spacer(
                            Modifier
                                .size(
                                    LocalConfiguration.current.screenWidthDp.dp
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
                                style = MaterialTheme.typography.headlineMedium,
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
                                style = MaterialTheme.typography.headlineMedium,
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
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground,
                    strokeWidth = 8.dp
                )
            }
        }
    }
}
