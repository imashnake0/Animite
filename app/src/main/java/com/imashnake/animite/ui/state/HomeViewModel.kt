package com.imashnake.animite.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.AnimeQuery
import com.imashnake.animite.data.repos.AnimeRepository
import com.imashnake.animite.data.repos.MediaListRepository
import com.imashnake.animite.type.MediaSeason
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
    private val animeRepository: AnimeRepository,
    private val mediaListRepository: MediaListRepository
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    // TODO: Understand coroutines better.
    private var fetchJob: Job? = null

    fun addAnimes(vararg id: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val animeList = mutableListOf<AnimeQuery.Media?>()
                for (i in id) {
                    animeList.add(animeRepository.fetchAnime(i))
                }

                val trendingAnime = mediaListRepository.fetchTrendingNowMediaList(MediaType.ANIME)

                val popularAnimeThisSeason = mediaListRepository.fetchPopularThisSeasonMediaList(
                    mediaType = MediaType.ANIME,
                    season = MediaSeason.SPRING,
                    seasonYear = Clock.System.todayAt(TimeZone.currentSystemDefault()).year
                )

                uiState = uiState.copy(animeList = animeList)
                uiState = uiState.copy(trendingAnimeList = trendingAnime)
                uiState = uiState.copy(popularAnimeThisSeasonList = popularAnimeThisSeason)
            } catch (ioe: IOException) {
                TODO()
            }
        }
    }
}
