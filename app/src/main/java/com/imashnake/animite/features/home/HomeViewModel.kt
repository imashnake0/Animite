package com.imashnake.animite.features.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.data.repos.MediaListRepository
import com.imashnake.animite.data.sauce.db.model.ListTag
import com.imashnake.animite.dev.ext.nextSeason
import com.imashnake.animite.dev.ext.season
import com.imashnake.animite.type.MediaSort
import com.imashnake.animite.type.MediaType
import dagger.hilt.android.lifecycle.HiltViewModel
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
        .flatMapLatest { mediaType ->
            mediaListRepository.getMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.TRENDING_DESC),
                tag = ListTag.TRENDING
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(0), null)

    val popularMediaThisSeason = mediaType
        .filterNotNull()
        .combine(now, ::Pair)
        .flatMapLatest { (mediaType, now) ->
            mediaListRepository.getMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.POPULARITY_DESC),
                tag = ListTag.POPULAR_SEASON,
                season = now.month.season,
                seasonYear = now.year
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(0), null)

    val upcomingMediaNextSeason = mediaType
        .filterNotNull()
        .combine(now, ::Pair)
        .flatMapLatest { (mediaType, now) ->
            mediaListRepository.getMediaList(
                mediaType = mediaType,
                tag = ListTag.UPCOMING,
                sort = listOf(MediaSort.POPULARITY_DESC),
                season = now.month.season.nextSeason(now).first,
                seasonYear = now.month.season.nextSeason(now).second
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(0), null)

    val allTimePopular = mediaType
        .filterNotNull()
        .flatMapLatest { mediaType ->
            mediaListRepository.getMediaList(
                mediaType = mediaType,
                tag = ListTag.POPULAR_ALL_TIME,
                sort = listOf(MediaSort.POPULARITY_DESC)
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(0), null)
}
