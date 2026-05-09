package com.imashnake.animite.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.MediaList.Type
import com.imashnake.animite.api.anilist.sanitize.settings.Prefs
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.resource.Resource.Companion.asResource
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
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class MangaViewModel @Inject constructor(
    private val mediaListRepository: AnilistMediaRepository,
    preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val refreshTrigger = MutableSharedFlow<Unit>()

    var useNetwork = false

    val prefs = combine(
        flow = preferencesRepository.isNsfwEnabled.filterNotNull(),
        flow2 = preferencesRepository.language.filterNotNull(),
        transform = { isNsfwEnabled, language ->
            Prefs(isNsfwEnabled, Media.Language.valueOf(language))
        }
    )

    val trendingMedia = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = prefs,
        transform = ::Pair
    ).flatMapLatest { (_, prefs) ->
        mediaListRepository.fetchMediaList(
            mediaListType = Type.TRENDING_NOW,
            mediaType = MediaType.MANGA,
            sort = listOf(MediaSort.TRENDING_DESC),
            useNetwork = useNetwork,
            isNsfwEnabled = prefs.isNsfwEnabled,
            language = prefs.language
        )
    }
    .asResource()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val allTimePopular = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = prefs,
        transform = ::Pair
    ).flatMapLatest { (_, prefs) ->
        mediaListRepository.fetchMediaList(
            mediaListType = Type.ALL_TIME_POPULAR,
            mediaType = MediaType.MANGA,
            sort = listOf(MediaSort.POPULARITY_DESC),
            useNetwork = useNetwork,
            isNsfwEnabled = prefs.isNsfwEnabled,
            language = prefs.language
        )
    }
    .asResource()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val isLoading = combineTransform(
        listOf(trendingMedia, allTimePopular)
    ) { resources ->
        emit(!resources.none { it is Resource.Loading<*> })
    }.stateIn(viewModelScope, SharingStarted.Lazily, true)

    fun refresh(setIsRefreshing: (Boolean) -> Unit) = viewModelScope.launch {
        setIsRefreshing(true)
        useNetwork = true
        refreshTrigger.emit(Unit)
        delay(1500L)
        useNetwork = false
        setIsRefreshing(false)
    }
}
