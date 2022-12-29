package com.imashnake.animite.features.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.Resource
import com.imashnake.animite.data.repos.MediaListRepository
import com.imashnake.animite.dev.ext.nextSeason
import com.imashnake.animite.dev.ext.season
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaListRepository: MediaListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mediaType = savedStateHandle.getStateFlow<MediaType?>("mediaType", null)
    private val now = savedStateHandle.getStateFlow("now", LocalDate.now())

    fun setMediaType(mediaType: MediaType) {
        savedStateHandle["mediaType"] = mediaType
    }

    val trendingMedia = mediaType
        .filterNotNull()
        .map { mediaType ->
            mediaListRepository.fetchMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.TRENDING_DESC),
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val popularMediaThisSeason = mediaType
        .filterNotNull()
        .combine(now, ::Pair)
        .map { (mediaType, now) ->
            mediaListRepository.fetchMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.POPULARITY_DESC),
                season = now.month.season,
                seasonYear = now.year
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val upcomingMediaNextSeason = mediaType
        .filterNotNull()
        .combine(now, ::Pair)
        .map { (mediaType, now) ->
            mediaListRepository.fetchMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.POPULARITY_DESC),
                season = now.month.season.nextSeason(now).first,
                seasonYear = now.month.season.nextSeason(now).second
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val allTimePopular = mediaType
        .filterNotNull()
        .map { mediaType ->
            mediaListRepository.fetchMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.POPULARITY_DESC)
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val isLoading = combineTransform(listOf(trendingMedia, popularMediaThisSeason, upcomingMediaNextSeason, allTimePopular)) {
        emit(it.any { it is Resource.Loading })
    }.stateIn(viewModelScope, SharingStarted.Lazily, true)

}
