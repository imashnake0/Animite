package com.imashnake.animite.anime

import androidx.lifecycle.SavedStateHandle
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
import com.imashnake.animite.media.ext.nextSeason
import com.imashnake.animite.media.ext.nextSeasonYear
import com.imashnake.animite.media.ext.season
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AnimeViewModel @Inject constructor(
    private val mediaListRepository: AnilistMediaRepository,
    savedStateHandle: SavedStateHandle,
    preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val now = savedStateHandle.getStateFlow(
        key = NOW,
        initialValue = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
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
            mediaType = MediaType.ANIME,
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

    val popularMediaThisSeason = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = now,
        flow3 = prefs,
        transform = ::Triple,
    ).flatMapLatest { (_, now, prefs) ->
        mediaListRepository.fetchMediaList(
            mediaListType = Type.POPULAR_THIS_SEASON,
            mediaType = MediaType.ANIME,
            sort = listOf(MediaSort.POPULARITY_DESC),
            season = now.month.season,
            seasonYear = now.year,
            useNetwork = useNetwork,
            isNsfwEnabled = prefs.isNsfwEnabled,
            language = prefs.language
        )
    }.asResource().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = Resource.loading(),
    )

    val upcomingMediaNextSeason = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = now,
        flow3 = prefs,
        transform = ::Triple,
    ).flatMapLatest { (_, now, prefs) ->
        val season = now.month.season
        mediaListRepository.fetchMediaList(
            mediaListType = Type.UPCOMING_NEXT_SEASON,
            mediaType = MediaType.ANIME,
            sort = listOf(MediaSort.POPULARITY_DESC),
            season = season.nextSeason,
            seasonYear = season.nextSeasonYear(now),
            useNetwork = useNetwork,
            isNsfwEnabled = prefs.isNsfwEnabled,
            language = prefs.language
        )
    }.asResource().stateIn(
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
            mediaType = MediaType.ANIME,
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
        listOf(trendingMedia, popularMediaThisSeason, upcomingMediaNextSeason, allTimePopular)
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

    companion object {
        const val NOW = "now"
    }
}
