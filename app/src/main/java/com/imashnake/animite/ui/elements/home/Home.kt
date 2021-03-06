package com.imashnake.animite.ui.elements.home

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.ui.state.HomeViewModel
import com.imashnake.animite.ui.theme.Backdrop
import com.imashnake.animite.ui.theme.Text
import com.imashnake.animite.ui.theme.backdropShape
import com.imashnake.animite.ui.theme.manropeFamily
import com.imashnake.animite.R as Res

/**
 * TODO: Kdoc.
 */
@ExperimentalMaterial3Api
@Composable
fun Home(
    viewModel: HomeViewModel = viewModel()
) {
    viewModel.run {
        addAnimes()
    }

    val popularThisSeasonAnimeList = viewModel.uiState.popularAnimeThisSeasonList?.media
    val trendingNowAnimeList = viewModel.uiState.trendingAnimeList?.media

    when {
        trendingNowAnimeList != null && popularThisSeasonAnimeList != null -> {
            Box(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
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

                        MediaSmallRow(mediaList = trendingNowAnimeList)

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

                        MediaSmallRow(mediaList = popularThisSeasonAnimeList)

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
