package com.imashnake.animite.ui.elements.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.imashnake.animite.ui.state.HomeViewModel
import com.imashnake.animite.ui.theme.Backdrop
import com.imashnake.animite.ui.theme.Text
import com.imashnake.animite.ui.theme.manropeFamily

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
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

                TrendingNowAnimeSmallRow(mediaList = trendingNowAnimeList)

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

                PopularAnimeThisSeasonSmallRow(mediaList = popularThisSeasonAnimeList)
            }
        }
        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
