package com.imashnake.animite.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.MediaQuery
import com.imashnake.animite.data.repos.MediaRepository
import com.imashnake.animite.data.repos.MediaListRepository
import com.imashnake.animite.type.MediaSeason
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import java.io.IOException
import javax.inject.Inject

/**
 * TODO: Kdoc.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaListRepository: MediaListRepository
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    // TODO: Understand coroutines better.
    private var fetchJob: Job? = null

    fun addAnimes(mediaType: MediaType = MediaType.ANIME) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val trendingAnime = mediaListRepository.fetchMediaList(
                    mediaType = MediaType.ANIME,
                    page = 0,
                    perPage = 10,
                    sort = listOf(MediaSort.TRENDING_DESC),
                    season = null,
                    seasonYear = null
                )

                val popularAnimeThisSeason = mediaListRepository.fetchMediaList(
                    mediaType = MediaType.ANIME,
                    page = 0,
                    perPage = 10,
                    sort = listOf(MediaSort.POPULARITY_DESC),
                    season = MediaSeason.SPRING,
                    seasonYear = Clock.System.todayAt(TimeZone.currentSystemDefault()).year
                )

                uiState = with(uiState) {
                    copy(
                        trendingAnimeList = trendingAnime,
                        popularAnimeThisSeasonList = popularAnimeThisSeason
                    )
                }
            } catch (ioe: IOException) {
                TODO()
            }
        }
    }
}
