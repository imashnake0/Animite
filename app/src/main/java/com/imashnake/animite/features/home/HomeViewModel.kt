package com.imashnake.animite.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.repos.MediaListRepository
import com.imashnake.animite.features.home.HomeUiState
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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaListRepository: MediaListRepository
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private var fetchJob: Job? = null
    fun populateMediaLists(mediaType: MediaType) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val trendingMedia = mediaListRepository.fetchMediaList(
                    mediaType = mediaType,
                    page = 0,
                    perPage = 10,
                    sort = listOf(MediaSort.TRENDING_DESC),
                    season = null,
                    seasonYear = null
                )

                val popularMediaThisSeason = if (mediaType == MediaType.ANIME) {
                    mediaListRepository.fetchMediaList(
                        mediaType = mediaType,
                        page = 0,
                        perPage = 10,
                        sort = listOf(MediaSort.POPULARITY_DESC),
                        season = MediaSeason.SPRING,
                        seasonYear = Clock.System.todayAt(TimeZone.currentSystemDefault()).year
                    )
                } else {
                    // TODO: This is needed because there is no "Popular This Season" for manga.
                    //  The title for the list, however, should be "All Time Popular".
                    mediaListRepository.fetchMediaList(
                        mediaType = mediaType,
                        page = 0,
                        perPage = 10,
                        sort = listOf(MediaSort.POPULARITY_DESC),
                        season = null,
                        seasonYear = null
                    )
                }

                uiState = with(uiState) {
                    copy(
                        trendingMediaList = trendingMedia,
                        popularMediaThisSeasonList = popularMediaThisSeason
                    )
                }
            } catch (ioe: IOException) {
                TODO()
            }
        }
    }
}
