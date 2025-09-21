package com.imashnake.animite.features.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.media.MediaList.Type
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.core.Constants
import com.imashnake.animite.core.data.Resource
import com.imashnake.animite.core.data.Resource.Companion.asResource
import com.imashnake.animite.dev.ext.nextSeason
import com.imashnake.animite.dev.ext.season
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
    private val refreshTrigger = MutableSharedFlow<Unit>()

    var useNetwork = false

    val trendingMedia = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = mediaType.filterNotNull(),
        transform = ::Pair,
    ).flatMapLatest { mediaType ->
        mediaListRepository.fetchMediaList(
            mediaListType = Type.TRENDING_NOW,
            mediaType = mediaType.second,
            sort = listOf(MediaSort.TRENDING_DESC),
            useNetwork = useNetwork,
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val popularMediaThisSeason = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = mediaType.filterNotNull(),
        flow3 = now,
        transform = ::Triple,
    ).flatMapLatest { (_, mediaType, now) ->
        mediaListRepository.fetchMediaList(
            mediaListType = Type.POPULAR_THIS_SEASON,
            mediaType = mediaType,
            sort = listOf(MediaSort.POPULARITY_DESC),
            season = now.month.season,
            seasonYear = now.year,
            useNetwork = useNetwork,
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val upcomingMediaNextSeason = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = mediaType.filterNotNull(),
        flow3 = now,
        transform = ::Triple,
    ).flatMapLatest { (_, mediaType, now) ->
        mediaListRepository.fetchMediaList(
            mediaListType = Type.UPCOMING_NEXT_SEASON,
            mediaType = mediaType,
            sort = listOf(MediaSort.POPULARITY_DESC),
            season = now.month.season.nextSeason(now).first,
            seasonYear = now.month.season.nextSeason(now).second,
            useNetwork = useNetwork,
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val allTimePopular = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = mediaType.filterNotNull(),
        transform = ::Pair,
    ).flatMapLatest { (_, mediaType) ->
        mediaListRepository.fetchMediaList(
            mediaListType = Type.ALL_TIME_POPULAR,
            mediaType = mediaType,
            sort = listOf(MediaSort.POPULARITY_DESC),
            useNetwork = useNetwork,
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val isLoading = combineTransform(
        listOf(trendingMedia, popularMediaThisSeason, upcomingMediaNextSeason, allTimePopular)
    ) { resources ->
        emit(!resources.none { it is Resource.Loading<*> })
    }.stateIn(viewModelScope, SharingStarted.Lazily, true)

    fun setMediaType(mediaType: MediaType) {
        savedStateHandle[Constants.MEDIA_TYPE] = mediaType
    }

    fun refresh(setIsRefreshing: (Boolean) -> Unit) = viewModelScope.launch {
        setIsRefreshing(true)
        useNetwork = true
        refreshTrigger.emit(Unit)
        delay(1500L)
        useNetwork = false
        setIsRefreshing(false)
    }

    companion object {
        const val NOW = "now"
    }
}
