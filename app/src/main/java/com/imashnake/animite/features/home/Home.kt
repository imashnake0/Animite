package com.imashnake.animite.features.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imashnake.animite.features.destinations.MediaPageDestination
import com.imashnake.animite.features.theme.Backdrop
import com.imashnake.animite.features.theme.Text
import com.imashnake.animite.features.theme.backdropShape
import com.imashnake.animite.features.theme.manropeFamily
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
                        painter = painterResource(id = Res.drawable.background),
                        contentDescription = "Background",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "おかえり",
                        color = Text,
                        fontSize = 57.sp,
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
                            .background(Backdrop)
                    ) {
                        Spacer(modifier = Modifier.size(24.dp))

                        Text(
                            text = "Trending Now",
                            color = Text,
                            fontSize = 14.sp,
                            fontFamily = manropeFamily,
                            fontWeight = FontWeight.Bold,
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
                            color = Text,
                            fontSize = 14.sp,
                            fontFamily = manropeFamily,
                            fontWeight = FontWeight.Bold,
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

                        Spacer(modifier = Modifier.size(104.dp))
                    }
                }
            }
        }
        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Backdrop)
            ) {
                CircularProgressIndicator(
                    color = Text,
                    strokeWidth = 8.dp
                )
            }
        }
    }
}
