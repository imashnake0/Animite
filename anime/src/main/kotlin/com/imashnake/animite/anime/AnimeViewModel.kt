package com.imashnake.animite.anime

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.anime.combine
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.explore.FilterStrategy
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.api.anilist.sanitize.settings.Prefs
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.model.AnimeLists
import com.imashnake.animite.core.resource.Resource
import com.imashnake.animite.core.resource.Resource.Companion.asResource
import com.imashnake.animite.media.ext.nextSeason
import com.imashnake.animite.media.ext.nextSeasonYear
import com.imashnake.animite.media.ext.season
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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
import kotlin.collections.emptyList
import kotlin.let
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
            useNetwork = useNetwork,
            filterStrategy = FilterStrategy(
                mediaType = MediaType.ANIME,
                sort = listOf(MediaSort.TRENDING_DESC),
                isNsfwEnabled = prefs.isNsfwEnabled,
                language = prefs.language
            ),
        )
    }.asResource { AnimeLists.TRENDING_NOW to it }

    val popularMediaThisSeason = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = now,
        flow3 = prefs,
        transform = ::Triple,
    ).flatMapLatest { (_, now, prefs) ->
        mediaListRepository.fetchMediaList(
            useNetwork = useNetwork,
            filterStrategy = FilterStrategy(
                mediaType = MediaType.ANIME,
                sort = listOf(MediaSort.POPULARITY_DESC),
                season = now.month.season,
                year = now.year,
                isNsfwEnabled = prefs.isNsfwEnabled,
                language = prefs.language
            ),
        )
    }.asResource { AnimeLists.POPULAR_THIS_SEASON to it }

    val upcomingMediaNextSeason = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = now,
        flow3 = prefs,
        transform = ::Triple,
    ).flatMapLatest { (_, now, prefs) ->
        val season = now.month.season
        mediaListRepository.fetchMediaList(
            useNetwork = useNetwork,
            filterStrategy = FilterStrategy(
                mediaType = MediaType.ANIME,
                sort = listOf(MediaSort.POPULARITY_DESC),
                season = season.nextSeason,
                year = season.nextSeasonYear(now),
                isNsfwEnabled = prefs.isNsfwEnabled,
                language = prefs.language
            ),
        )
    }.asResource { AnimeLists.UPCOMING_NEXT_SEASON to it }

    val allTimePopular = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = prefs,
        transform = ::Pair
    ).flatMapLatest { (_, prefs) ->
        mediaListRepository.fetchMediaList(
            useNetwork = useNetwork,
            filterStrategy = FilterStrategy(
                mediaType = MediaType.ANIME,
                sort = listOf(MediaSort.POPULARITY_DESC),
                isNsfwEnabled = prefs.isNsfwEnabled,
                language = prefs.language
            ),
        )
    }.asResource { AnimeLists.ALL_TIME_POPULAR to it }

    val newlyAdded = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = prefs,
        transform = ::Pair
    ).flatMapLatest { (_, prefs) ->
        mediaListRepository.fetchMediaList(
            useNetwork = useNetwork,
            filterStrategy = FilterStrategy(
                mediaType = MediaType.ANIME,
                sort = listOf(MediaSort.ID_DESC),
                isNsfwEnabled = prefs.isNsfwEnabled,
                language = prefs.language
            ),
        )
    }.asResource { AnimeLists.NEWLY_ADDED to it }

    val titlesIndices = preferencesRepository.animeListsIndices

    val lists = combine(
        trendingMedia,
        popularMediaThisSeason,
        upcomingMediaNextSeason,
        allTimePopular,
        newlyAdded,
        preferencesRepository.animeListsIndices,
    ) { trendingMedia, popularMediaThisSeason, upcomingMediaNextSeason, allTimePopular, newlyAdded, indices ->
        mutableListOf(
            trendingMedia,
            popularMediaThisSeason,
            upcomingMediaNextSeason,
            allTimePopular,
            newlyAdded
        ).apply {
            indices?.let {
                // the bytes at the indices of indices represents the order of the lists
                this[it[0].toInt()] = trendingMedia
                this[it[1].toInt()] = popularMediaThisSeason
                this[it[2].toInt()] = upcomingMediaNextSeason
                this[it[3].toInt()] = allTimePopular
                this[it[4].toInt()] = newlyAdded
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = listOf(
            Resource.loading(),
            Resource.loading(),
            Resource.loading(),
            Resource.loading(),
            Resource.loading(),
        ),
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

inline fun <
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
        R
        > combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    crossinline transform: suspend (
        T1,
        T2,
        T3,
        T4,
        T5,
        T6,
    ) -> R,
): Flow<R> = combine(
    flow,
    flow2,
    flow3,
    flow4,
    flow5,
    flow6,
) { args: Array<*> ->
    @Suppress("UNCHECKED_CAST")
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6,
    )
}

