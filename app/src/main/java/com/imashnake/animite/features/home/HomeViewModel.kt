package com.imashnake.animite.features.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.data.Resource.Companion.asResource
import com.imashnake.animite.dev.ext.nextSeason
import com.imashnake.animite.dev.ext.season
import com.imashnake.animite.dev.internal.Constants
import com.imashnake.animite.dev.internal.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel @Inject constructor(
    private val mediaListRepository: AnilistMediaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mediaType = savedStateHandle.getStateFlow<MediaType?>(Constants.MEDIA_TYPE, null)
    private val now = savedStateHandle.getStateFlow(NOW, LocalDate.now())

    val trendingMedia = mediaType
        .filterNotNull()
        .flatMapLatest { mediaType ->
            mediaListRepository.fetchMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.TRENDING_DESC),
            )
        }
        .asResource { Section(header = TRENDING_NOW, body = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val popularMediaThisSeason = mediaType
        .filterNotNull()
        .combine(now, ::Pair)
        .flatMapLatest { (mediaType, now) ->
            mediaListRepository.fetchMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.POPULARITY_DESC),
                season = now.month.season,
                seasonYear = now.year
            )
        }
        .asResource { Section(header = POPULAR_THIS_SEASON, body = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val upcomingMediaNextSeason = mediaType
        .filterNotNull()
        .combine(now, ::Pair)
        .flatMapLatest { (mediaType, now) ->
            mediaListRepository.fetchMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.POPULARITY_DESC),
                season = now.month.season.nextSeason(now).first,
                seasonYear = now.month.season.nextSeason(now).second
            )
        }
        .asResource { Section(header = UPCOMING_NEXT_SEASON, body = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val allTimePopular = mediaType
        .filterNotNull()
        .flatMapLatest { mediaType ->
            mediaListRepository.fetchMediaList(
                mediaType = mediaType,
                sort = listOf(MediaSort.POPULARITY_DESC)
            )
        }
        .asResource { Section(header = ALL_TIME_POPULAR, body = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Resource.loading())

    val isLoading = combineTransform(
        listOf(trendingMedia, popularMediaThisSeason, upcomingMediaNextSeason, allTimePopular)
    ) { resources ->
        emit(!resources.none { it is Resource.Loading<*> })
    }.stateIn(viewModelScope, SharingStarted.Lazily, true)

    fun setMediaType(mediaType: MediaType) {
        savedStateHandle[Constants.MEDIA_TYPE] = mediaType
    }

    companion object {
        const val NOW = "now"
        const val TRENDING_NOW = "Trending Now"
        const val POPULAR_THIS_SEASON = "Popular This Season"
        const val UPCOMING_NEXT_SEASON = "Upcoming Next Season"
        const val ALL_TIME_POPULAR = "All Time Popular"
    }
}
