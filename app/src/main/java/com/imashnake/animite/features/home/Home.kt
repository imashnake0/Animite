package com.imashnake.animite.features.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.dev.ext.bottomNavigationBarPadding
import com.imashnake.animite.dev.ext.bottomNavigationBarSize
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.features.theme.AnimiteTheme
import com.imashnake.animite.features.theme.backdropShape
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
            AnimiteTheme {
                Box(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                ) {
                    Box {
                        Image(
                            painter = painterResource(id = Res.drawable.background),
                            contentDescription = "Background",
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
                            text = "おかえり",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 24.dp, bottom = (24 + 18).dp)
                        )
                    }

                    // TODO: Use `verticalArrangement` instead of the `Spacer`s.
                    Column {
                        Spacer(
                            modifier = Modifier.size((LocalConfiguration.current.screenWidthDp - 18).dp)
                        )

                        Column(
                            modifier = Modifier
                                .clip(backdropShape)
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            Spacer(modifier = Modifier.size(24.dp))

                            Text(
                                text = "Trending Now",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(start = 24.dp)
                            )

                            Spacer(modifier = Modifier.size(12.dp))

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

                            Spacer(modifier = Modifier.size(24.dp))

                            Text(
                                text = "Popular This Season",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(start = 24.dp)
                            )

                            Spacer(modifier = Modifier.size(12.dp))

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

                            Spacer(modifier = Modifier.height(bottomNavigationBarSize + 24.dp))
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
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground,
                    strokeWidth = 8.dp
                )
            }
        }
    }
}
