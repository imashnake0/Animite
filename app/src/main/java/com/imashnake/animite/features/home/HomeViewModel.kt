package com.imashnake.animite.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.repos.MediaListRepository
import com.imashnake.animite.dev.ext.nextSeason
import com.imashnake.animite.dev.ext.season
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaListRepository: MediaListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mediaType = savedStateHandle.getStateFlow<MediaType?>("mediaType", null)

    private val now = mediaType
        .filterNotNull()
        .map { LocalDate.now() }

    fun setMediaType(mediaType: MediaType) {
        savedStateHandle["mediaType"] = mediaType
    }

    val trendingMedia = mediaType
        .filterNotNull()
        .flatMapLatest {
            mediaListRepository.getMediaList(it, 0, 10, listOf(MediaSort.TRENDING_DESC), null, null)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val popularMediaThisSeason = mediaType
        .filterNotNull()
        .combine(now, ::Pair)
        .flatMapLatest { (mediaType, now) ->
            mediaListRepository.getMediaList(mediaType, 0, 10, listOf(MediaSort.POPULARITY_DESC), now.month.season, now.year)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val upcomingMediaNextSeason = mediaType
        .filterNotNull()
        .combine(now, ::Pair)
        .flatMapLatest { (mediaType, now) ->
            mediaListRepository.getMediaList(mediaType, 0, 10, listOf(MediaSort.POPULARITY_DESC), now.month.season.nextSeason(now).first, now.month.season.nextSeason(now).second)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val allTimePopular = mediaType
        .filterNotNull()
        .flatMapLatest { mediaType ->
            mediaListRepository.getMediaList(mediaType, 0, 10, listOf(MediaSort.POPULARITY_DESC), null, null)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    var uiState by mutableStateOf(HomeUiState())
        private set

    private var fetchJob: Job? = null
    /*fun populateMediaLists(mediaType: MediaType) {


        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val trendingMedia = mediaListRepository.getMediaList(
                    mediaType = mediaType,
                    page = 0,
                    perPage = 10,
                    sort = listOf(MediaSort.TRENDING_DESC),
                    season = null,
                    seasonYear = null
                )

                val now = LocalDate.now()
                val popularMediaThisSeason = mediaListRepository.getMediaList(
                    mediaType = mediaType,
                    page = 0,
                    perPage = 10,
                    sort = listOf(MediaSort.POPULARITY_DESC),
                    season = now.month.season,
                    seasonYear = now.year
                )

                val upcomingMediaNextSeason = mediaListRepository.getMediaList(
                    mediaType = mediaType,
                    page = 0,
                    perPage = 10,
                    sort = listOf(MediaSort.POPULARITY_DESC),
                    season = now.month.season.nextSeason(now).first,
                    seasonYear = now.month.season.nextSeason(now).second
                )

                val allTimePopularMedia = mediaListRepository.getMediaList(
                    mediaType = mediaType,
                    page = 0,
                    perPage = 10,
                    sort = listOf(MediaSort.POPULARITY_DESC),
                    season = null,
                    seasonYear = null
                )

                uiState = with(uiState) {
                    copy(
                        trendingList = trendingMedia,
                        popularList = popularMediaThisSeason,
                        upcomingList = upcomingMediaNextSeason,
                        allTimePopularList = allTimePopularMedia
                    )
                }
            } catch (ioe: IOException) {
                TODO()
            }
        }
    }*/
}
