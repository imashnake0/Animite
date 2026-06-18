package com.imashnake.animite.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imashnake.animite.api.anilist.AnilistMediaRepository
import com.imashnake.animite.api.anilist.sanitize.explore.FilterStrategy
import com.imashnake.animite.api.anilist.sanitize.media.Media
import com.imashnake.animite.api.anilist.sanitize.media.MediaList
import com.imashnake.animite.api.anilist.sanitize.settings.Prefs
import com.imashnake.animite.api.anilist.type.MediaSort
import com.imashnake.animite.api.anilist.type.MediaType
import com.imashnake.animite.api.preferences.PreferencesRepository
import com.imashnake.animite.core.model.MangaList
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
import org.jetbrains.compose.resources.StringResource
import javax.inject.Inject
import kotlin.collections.mapNotNull
import kotlin.collections.orEmpty

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
            useNetwork = useNetwork,
            filterStrategy = FilterStrategy(
                mediaType = MediaType.MANGA,
                sort = listOf(MediaSort.TRENDING_DESC),
                isNsfwEnabled = prefs.isNsfwEnabled,
                language = prefs.language
            ),
        )
    }.asResource { MangaRow(MangaList.TRENDING_NOW.res, it) }

    val allTimePopular = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = prefs,
        transform = ::Pair
    ).flatMapLatest { (_, prefs) ->
        mediaListRepository.fetchMediaList(
            useNetwork = useNetwork,
            filterStrategy = FilterStrategy(
                mediaType = MediaType.MANGA,
                sort = listOf(MediaSort.POPULARITY_DESC),
                isNsfwEnabled = prefs.isNsfwEnabled,
                language = prefs.language
            ),
        )
    }.asResource { MangaRow(MangaList.ALL_TIME_POPULAR.res, it) }

    val newlyAdded = combine(
        flow = refreshTrigger.onStart { emit(Unit) },
        flow2 = prefs,
        transform = ::Pair
    ).flatMapLatest { (_, prefs) ->
        mediaListRepository.fetchMediaList(
            useNetwork = useNetwork,
            filterStrategy = FilterStrategy(
                mediaType = MediaType.MANGA,
                sort = listOf(MediaSort.ID_DESC),
                isNsfwEnabled = prefs.isNsfwEnabled,
                language = prefs.language
            ),
        )
    }.asResource { MangaRow(MangaList.NEWLY_ADDED.res, it) }

    val lists = combine(
        trendingMedia,
        allTimePopular,
        newlyAdded,
        preferencesRepository.mangaListsIndices,
    ) { trendingNow, allTimePopular, newlyAdded, indices ->
        indices?.toMutableList().orEmpty().mapNotNull {
            when (it.toInt()) {
                1 -> trendingNow
                2 -> allTimePopular
                3 -> newlyAdded
                else -> null
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = listOf(
            Resource.loading(),
            Resource.loading(),
            Resource.loading(),
        ),
    )

    val isLoading = combineTransform(
        listOf(trendingMedia, allTimePopular, newlyAdded)
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

data class MangaRow(
    val title: StringResource,
    val mediaList: MediaList,
)
